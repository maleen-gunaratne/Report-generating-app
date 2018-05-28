/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ebuilder.picard;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
//import myproject.Step;
//import com.ebuilder.picard.Step;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonConverter {
    
    JSONArray converter(String source){
              
   final  String REGEX= "((\\/home\\/ebuilder\\/screenshots)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.)(.))";
               
              try {
            boolean base_story_status = false;
            boolean main_story_status = false;
            boolean error_status = false;
            boolean failures = false;           //

            JSONArray main_story_list = new JSONArray();  // main_story_list =myArray
            JSONObject properties = new JSONObject();
            JSONObject main_story = null;     //main_story=myObject
            JSONArray base_stories = null;   // base_stories = mainArray
            JSONArray base_story = null;      //  base_story = baseArray
            JSONObject base_step = null;     // base_step = basestep

            String start_main_story = "!-- Start Main Story:";
            String end_main_story = "!-- End Main Story:";
            String start_base_story = "!-- Start Base Story:";
            String end_base_story = "!-- End Base Story:";
            
          //  File fXmlFile = new File("C:/Users/maleen/oneMain.xml");
         //   File fXmlFile = new File("C:/Users/maleen/update.xml");
         // File fXmlFile = new File("C:/Users/maleen/stream2.xml");
          //   File fXmlFile = new File("C:/Users/maleen/new.xml");
            File fXmlFile = new File(source);
          
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);

            doc.getDocumentElement().normalize();
            System.out.println("Root element :" + doc.getDocumentElement().getNodeName()); //testsuit
            
            NodeList nList1 = doc.getElementsByTagName("property");   //
             System.out.println("number of properties tags :"+nList1.getLength());
               for (int temp = 0; temp < nList1.getLength(); temp++) {
                Node nNode = nList1.item(temp);
                 if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                     if(eElement.getAttribute("name").contains("host.url")){ 
                         properties.put("host_url",eElement.getAttribute("value"));
                     }          
                     if(eElement.getAttribute("name").contains("parallel.agent.number")){                
                         properties.put("parallel_agent_number",eElement.getAttribute("value"));
                     } 
                     if(eElement.getAttribute("name").contains("story.filter")){
                         properties.put("story_filter",eElement.getAttribute("value"));
                     }
                 }
             }    
                main_story_list.put(properties);  
         
            NodeList nList = doc.getElementsByTagName("testcase");      
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                //System.out.println("\nCurrent Element :" + nNode.getNodeName());//staff
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    
                    if (eElement.getAttribute("name").contains(start_main_story)) {
                        main_story_status = true;
                        main_story = new JSONObject();
                        base_stories = new JSONArray();
                        main_story.put("main_story_name", eElement.getAttribute("name"));
                    }
                    if (eElement.getAttribute("name").contains(end_main_story)) {
                        main_story_status = false;
                        failures = false;  //
                    }   
                    
                    if (eElement.getAttribute("name").contains(start_base_story)) {
                        base_story_status = true;  
                               base_story = new JSONArray();    
                    }
                    if (eElement.getAttribute("name").contains(end_base_story)) {
                        base_story_status = false;
                    }

                    if (base_story_status == true) {
                        if (!failures) {         //To stop create Step objects by after meet failure
                            Step s = new Step();
                            base_step = new JSONObject();
                            s.setStep_name(eElement.getAttribute("name"));
                            s.setExecution_time(eElement.getAttribute("time"));

                            base_step.put("step_name", s.getStep_name());
                            base_step.put("execution_time", s.getExecution_time());

                        //In order to handle  failures  in 'failure' Tag
                            if (eElement.getElementsByTagName("failure").item(0) != null) {
                                base_step.put("error_name", eElement.getElementsByTagName("failure").item(0).getTextContent());
                                error_status = true;
                                main_story.put("error", eElement.getElementsByTagName("failure").item(0).getTextContent());
                                
                        //In order to get screenshot_path        
                                if(eElement.getElementsByTagName("system-out").item(0)!=null){   
                                   Pattern pattern = Pattern.compile(REGEX);
                                   Matcher matcher = pattern.matcher(eElement.getElementsByTagName("system-out").item(0).getTextContent());
                                       while(matcher.find()) {
                                       System.out.println(matcher.group(0));
                                       base_step.put("screenshot_path", matcher.group(0));
                                  
                                       }
                                }
                            }
                        //In order to handle  failures  in 'error' Tag  
                           if (eElement.getElementsByTagName("error").item(0) != null) {
                                base_step.put("error_name", eElement.getElementsByTagName("error").item(0).getTextContent());
                                error_status = true;
                                main_story.put("error", eElement.getElementsByTagName("error").item(0).getTextContent());                             
                            }
                            base_story.put(base_step);                        
                        }    
                    }
                    if (eElement.getElementsByTagName("failure").item(0) != null || eElement.getElementsByTagName("error").item(0)!=null) {
                        failures = true; 
                    }

                    if (main_story_status) {

                        if (base_story_status == false) {
                            base_stories.put(base_story);
                        }               
                        main_story.put("base_stories", base_stories);
                        main_story.put("error_status", error_status);
                    }
                    
                    if (eElement.getAttribute("name").contains(end_main_story)) {
                          main_story_list.put(main_story);
                          error_status=false;
                    }       
                }
            }       
      //     System.out.println(myArray);
            return main_story_list;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;     
    }    
}

