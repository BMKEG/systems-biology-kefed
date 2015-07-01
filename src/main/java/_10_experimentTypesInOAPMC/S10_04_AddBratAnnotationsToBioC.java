package _10_experimentTypesInOAPMC;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.resource.metadata.TypeSystemDescription;
import org.cleartk.opennlp.tools.SentenceAnnotator;
import org.cleartk.token.tokenizer.TokenAnnotator;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.uimafit.factory.AggregateBuilder;
import org.uimafit.factory.AnalysisEngineFactory;
import org.uimafit.factory.CollectionReaderFactory;
import org.uimafit.factory.TypeSystemDescriptionFactory;
import org.uimafit.pipeline.SimplePipeline;

import edu.isi.bmkeg.digitalLibrary.cleartk.annotators.AddBratAnnotations;
import edu.isi.bmkeg.digitalLibrary.cleartk.annotators.AddFragmentsAndCodes;
import edu.isi.bmkeg.uimaBioC.uima.readers.BioCCollectionReader;
import edu.isi.bmkeg.uimaBioC.uima.readers.Nxml2TxtFilesCollectionReader;

/**
 * This script runs through serialized JSON files from the model and converts
 * them to VPDMf KEfED models, including the data.
 * 
 * @author Gully
 * 
 */
public class S10_04_AddBratAnnotationsToBioC {

	public static class Options {

		@Option(name = "-inDir", usage = "Input Directory", required = true, metaVar = "IN-DIRECTORY")
		public File inDir;

		//~~~~~~~~~~~~~~~~~~~~~~~~~
		
		@Option(name = "-bratDir", usage = "Brat directory", required = true, metaVar = "LOGIN")
		public File bratDir;

		@Option(name = "-bratType", usage = "Type of these annotations", required = true, metaVar = "TYPE")
		public String bratType;

		//~~~~~~~~~~~~~~~~~~~~~~~~~
		
		@Option(name = "-outDir", usage = "Output Directory", required = true, metaVar = "OUT-DIRECTORY")
		public File outDir;

		@Option(name = "-outFormat", usage = "Output Format", required = true, metaVar = "XML/JSON")
		public String outFormat;

	}

	private static Logger logger = Logger
			.getLogger(S10_04_AddBratAnnotationsToBioC.class);

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		Options options = new Options();

		CmdLineParser parser = new CmdLineParser(options);

		try {

			parser.parseArgument(args);

		} catch (CmdLineException e) {

			System.err.println(e.getMessage());
			System.err.print("Arguments: ");
			parser.printSingleLineUsage(System.err);
			System.err.println("\n\n Options: \n");
			parser.printUsage(System.err);
			System.exit(-1);

		}
		
		if (!options.outDir.getParentFile().exists())
			options.outDir.getParentFile().mkdirs();

		TypeSystemDescription typeSystem = TypeSystemDescriptionFactory
				.createTypeSystemDescription("bioc.TypeSystem");

		CollectionReader cr = CollectionReaderFactory.createCollectionReader(
				BioCCollectionReader.class, typeSystem,
				BioCCollectionReader.INPUT_DIRECTORY, options.inDir,
				BioCCollectionReader.OUTPUT_DIRECTORY, options.outDir,
				BioCCollectionReader.PARAM_FORMAT, BioCCollectionReader.JSON);
		
		AggregateBuilder builder = new AggregateBuilder();

		builder.add(SentenceAnnotator.getDescription()); // Sentence
														// segmentation
		builder.add(TokenAnnotator.getDescription()); // Tokenization
		 
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				AddBratAnnotations.class, 
				AddBratAnnotations.BRAT_DATA_DIRECTORY, options.bratDir, 
				AddBratAnnotations.BRAT_TYPE, options.bratType  ));
		 
		String outFormat = null;
		if( options.outFormat.toLowerCase().equals("xml") ) 
			outFormat = SaveAsBioCDocuments.XML;
		else if( options.outFormat.toLowerCase().equals("json") ) 
			outFormat = SaveAsBioCDocuments.JSON;
		else 
			throw new Exception("Output format " + options.outFormat + " not recognized");
		
		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				AddBioCPassagesAndAnnotationsToDocuments.class));

		builder.add(AnalysisEngineFactory.createPrimitiveDescription(
				SaveAsBioCDocuments.class, 
				SaveAsBioCDocuments.PARAM_FILE_PATH,
				options.outDir.getPath(),
				SaveAsBioCDocuments.PARAM_FORMAT,
				outFormat));

		SimplePipeline.runPipeline(cr, builder.createAggregateDescription());

	}

}
