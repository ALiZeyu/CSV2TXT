package cn.edu.nju.ws.GeoScholar.templating.common;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.parser.nndep.DependencyParser;
import edu.stanford.nlp.process.DocumentPreprocessor;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.TypedDependency;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

public class Depparser {

	private static MaxentTagger tagger;
	private static DependencyParser parser;
	private static String modelPath = "edu/stanford/nlp/models/parser/nndep/CTB_CoNLL_params.txt.gz";
	private static String taggerPath = "edu/stanford/nlp/models/pos-tagger/chinese-distsim/chinese-distsim.tagger";
	
	public static void init() {
	    tagger = new MaxentTagger(taggerPath);
	    parser = DependencyParser.loadFromModelFile(modelPath);
	}
	
	protected static DepTree depparse (String text) {
	    List<DepTree> list = new ArrayList<DepTree>();
	    list.add(new DepTree(0, "ROOT"));
	    DocumentPreprocessor tokenizer = new DocumentPreprocessor(new StringReader(text));
	    List<TypedDependency> l = null;
	    for (List<HasWord> sentence : tokenizer) {
	      List<TaggedWord> tagged = tagger.tagSentence(sentence);
	      int i = 1;
	      for (TaggedWord tw : tagged) {
	    	  list.add(new DepTree(i, tw.word()));
	    	  i++;
	      }
	      GrammaticalStructure gs = parser.predict(tagged);
	      l = (List<TypedDependency>)gs.typedDependencies();
	    }
	    if (l != null)
	    for (TypedDependency td : l) {
	    	list.get(td.gov().index()).child.add(list.get(td.dep().index()));
	    	list.get(td.gov().index()).rel.add(td.reln().getShortName());
	    }
	    return list.get(0);
	}
}
