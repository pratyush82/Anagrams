/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary
{
    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    ArrayList<String> wordList = new ArrayList<String>();
    HashSet<String> wordSet = new HashSet<String>();
    HashMap<String,ArrayList<String>> letterToWord = new HashMap<String,ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizeToWord = new HashMap<Integer, ArrayList<String>>();
    int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException
    {
        BufferedReader in = new BufferedReader(reader);
        String line;

        while ((line = in.readLine()) != null)
        {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String s = sortLetters(word);
            if(letterToWord.containsKey(s))
            {
                letterToWord.get(s).add(word);
            }
            else
            {
                ArrayList<String> res = new ArrayList<String>();
                res.add(word);
                letterToWord.put(s,res);
            }
            int sz = word.length();
            if(sizeToWord.containsKey(sz))
            {
                sizeToWord.get(sz).add(word);
            }
            else
            {
                ArrayList<String> res = new ArrayList<String>();
                res.add(word);
                sizeToWord.put(sz,res);
            }
        }
    }

    public boolean isGoodWord(String word, String base)
    {
        if(wordSet.contains(word) && word.indexOf(base) == -1)
            return true;
        return false;
    }

    public ArrayList<String> getAnagrams(String word, String targetWord)
    {
        ArrayList<String> result = new ArrayList<String>();
        String s = sortLetters(word);
        result = letterToWord.get(s);
        if(result != null)
        {
            for (int i = 0; i < result.size(); i++) {
                String str = result.get(i);
                if (!(isGoodWord(str, targetWord)))
                {
                    result.remove(str);
                    i--;
                }
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word)
    {
        ArrayList<String> result = new ArrayList<String>();
        for(char ch = 'a';ch <= 'z';ch++)
        {
            String w = word + ch;
            if(getAnagrams(w,word) != null)
                result.addAll(getAnagrams(w,word));
        }
        return result;
    }

    public String pickGoodStarterWord()
    {
        int s = sizeToWord.get(wordLength).size();
        int r = random.nextInt(s);
        for(int i = 0;i < s;i++)
        {
            int l = r + i;
            if(l >= s)
                l = l % s ;
            String str = sizeToWord.get(wordLength).get(l);
            if(getAnagramsWithOneMoreLetter(str).size() >= MIN_NUM_ANAGRAMS)
            {
                if((wordLength+1) <= MAX_WORD_LENGTH)
                    wordLength++;
                return str;
            }
        }
        return null;
    }

    public String sortLetters(String w)
    {
        char t[] = w.toCharArray();
        Arrays.sort(t);
        return new String(t);
    }
}
