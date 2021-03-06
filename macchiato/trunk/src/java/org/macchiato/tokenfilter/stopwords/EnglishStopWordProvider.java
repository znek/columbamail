//The contents of this file are subject to the Mozilla Public License Version 1.1
//(the "License"); you may not use this file except in compliance with the 
//License. You may obtain a copy of the License at http://www.mozilla.org/MPL/
//
//Software distributed under the License is distributed on an "AS IS" basis,
//WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License 
//for the specific language governing rights and
//limitations under the License.
//
//The Original Code is "The Columba Project"
//
//The Initial Developers of the Original Code are Frederik Dietz and Timo Stich.
//Portions created by Frederik Dietz and Timo Stich are Copyright (C) 2003. 
//
//All Rights Reserved.
package org.macchiato.tokenfilter.stopwords;


/**
 * @author fdietz
 */
public class EnglishStopWordProvider implements StopWordProvider {
    /**
     * English words which are usually not useful for further processing
     */
    private final static String[] stopwords = {
        "a", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in",
        "into", "is", "it", "no", "not", "of", "on", "or", "s", "such", "t",
        "that", "the", "their", "then", "there", "these", "they", "this", "to",
        "was", "will", "with", "from", "all", "have", "you", "can", "your", "an",
        "any", "our", "we", "email", "has", "do",
        // months
        "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug","Sep", "Oct", "Nov", "Dec",
        
    };

    /**
     * @see org.macchiato.tokenfilter.StopWordProvider#getStopWords()
     */
    public String[] getStopWords() {
        return stopwords;
    }
    
    /**
     * @see org.macchiato.tokenfilter.StopWordProvider#length()
     */
    public int length() {
       return stopwords.length;
    }

}
