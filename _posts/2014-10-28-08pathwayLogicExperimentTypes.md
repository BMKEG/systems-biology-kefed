---
title: Pathway Logic Experiment Types
description:  Here we describe working with the different assay types within the Pathway Logic database and how this might fit in to a KEfED-based strategy.  
layout: defaultTOC
prevPage: 07heuristicsForResultText.html
nextPage: 09initialResultsKEfEDEpistemicsStudy.html
---

Based on our simple parsing of html forms from the Pathway Logic database, we have identified ~1966 papers that have been curated into their system. 

Of these 1966, only 73 are available in the open access dataset. Here, we list these by assay type. These are also listed in the digital library under the Corpus labeled `PathwayLogicFullText`. 

<table>
<tr><td>Assay</td><td># papers in OA</td></tr>
 
Since `Coprecipitation` is the largest contributor in this list, we now list all figures from the OA data set that describe Coprecipitation experiments. This provides an initial training set that we may now attempt to model and understand with 

<table>
<th><td>pmid</td><td>figure</td></th>

An example of coprecipitation is shown below: 

From `Parvatiyar-2012-13-1155` (pmid: 23142775), Fig-5a

From page 1159, narrative text:
 
> The introduction of either c-di-GMP or c-di-AMP into D2SC cells led to enhanced formation of the DDX41-STING complex (Fig. 5a). 

Based on this figure: 

![](images/23142775-fig5a.jpg)

After manual curation, this provides this model:

This model generates this KEfED data structure:

![](images/23142775-fig5a-kefed.jpg)

Which when represented as a data structure looks like this:

	?protein-concentration 
		[?cell-type][?reagent][?duration][?primary-antibody][?primary-antibody] 

The experiment shows the behavior of  DDX41 / STING 
 complex concentration based on this value (which corresponds is the top row of the figure).  

	?protein-concentration 
		[D2SC][?reagent][4h][DDX41][STING] 

See this link for definitions of Pathway Logic Assays: [http://pl.csl.sri.com/CurationNotebook/index.html](http://pl.csl.sri.com/CurationNotebook/index.html) 

### Identifying the most relevant assays for Ras-based work.

From the 71 "open access" pmids, there are 1716 datums.
24 have Hras, Braf, Raf1 or Rac1 as subject.

These come from 8 papers: [16492808, 11448999, 11777939, 12515821, 19050761, 20929976, 16520382, 12876277]

The majority of these were about Rac1

They include the following assays
[copptby, GTP-association, phos, boundto, IVKA]

### Highest priority assays

* coprecipitation	
* Phosphorylation	
* ProteinExpression	
* inVitroKinase
* location
* GTPAssoc

## Open Access Phosphorylation Papers

23142775

