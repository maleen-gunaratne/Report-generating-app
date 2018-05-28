package com.ebuilder.picard;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HtmlConverter {

    JSONArray base_stories = null;   //main_array
    JSONObject step = null;
    JSONObject main_story = null;
    String step_name = null;
    String error_name = null;
    String screenshot_path = null;
    String execution_time = null;
    int numOfBaseStories = 0;  //numOfArraysInMainArray=numOfBaseStories
    int numOfSteps = 0;         // numOfObjInArray= numOfSteps
    int numOfMainStories = 0;  // int numOfObjInMyArray = 0;

    //report  variable contains base html tags  to create final web page   
    //test_data is the iterartive variable which  finaly append into report
    StringBuilder test_data = new StringBuilder();
    StringBuilder report = new StringBuilder();

    void converter(JSONArray source_list) {

        report.append("<html>\n"
                + "<head>\n"
                + "<style rel=\"stylesheet\" type=\"text/css\">\n"
                + ".panel-heading{"
                + "background-color:#b2d3ff;"
                + "text-align: center;}"
                + ".tree {\n"
                + "    min-height:300px;\n"
                + "    padding:0px;\n"
                + "    margin-bottom:5px;\n"
                + "    background-color:#5082e6;\n"
                + "    border:1px solid #999;\n"
                + "    -webkit-border-radius:4px;\n"
                + "    -moz-border-radius:4px;\n"
                + "    border-radius:4px;\n"
                + "    -webkit-box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05);\n"
                + "    -moz-box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05);\n"
                + "    box-shadow:inset 0 1px 1px rgba(0, 0, 0, 0.05)\n"
                + "}\n"
                + ".tree li {\n"
                + "    list-style-type:none;\n"
                + "    margin:0;\n"
                + "    padding:10px 5px 0 5px;\n"
                + "    position:relative\n"
                + "}\n"
                + ".tree li::before, .tree li::after {\n"
                + "    content:'';\n"
                + "    left:-20px;\n"
                + "    position:absolute;\n"
                + "    right:auto\n"
                + "}\n"
                + ".tree li::before {\n"
                + "    border-left:1px solid #999;\n"
                + "    bottom:50px;\n"
                + "    height:100%;\n"
                + "    top:0;\n"
                + "    width:1px\n"
                + "}\n"
                + ".tree li::after {\n"
                + "    border-top:1px solid #999;\n"
                + "    height:20px;\n"
                + "    top:25px;\n"
                + "    width:25px\n"
                + "}\n"
                + ".tree li span {\n"
                + "    -moz-border-radius:5px;\n"
                + "    -webkit-border-radius:5px;\n"
                + "    border:1px solid #999;\n"
                + "    border-radius:5px;\n"
                + "    display:inline-block;\n"
                + "    padding:3px 8px;\n"
                + "    text-decoration:none\n"
                + "}\n"
                + ".tree li.parent_li>span {\n"
                + "    cursor:pointer\n"
                + "}\n"
                + ".tree>ul>li::before, .tree>ul>li::after {\n"
                + "    border:0\n"
                + "}\n"
                + ".tree li:last-child::before {\n"
                + "    height:30px\n"
                + "}\n"
                + ".tree li.parent_li>span:hover, .tree li.parent_li>span:hover+ul li span {\n"
                + "    background:#eee;\n"
                + "    border:1px solid #94a0b4;\n"
                + "    color:#000\n"
                + "}\n"
                + "</style>\n"
                + "<link rel=\"stylesheet\" href=\"http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css\"> \n"
                + "<script src=\"http://netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/js/bootstrap.min.js\"></script>\n"
                + "<script src=\"https://code.jquery.com/jquery-3.2.1.slim.min.js\"></script>\n"
                + "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.12.9/umd/popper.min.js\"></script>\n"
                + "<script>\n"
                + "    $(function () {\n"
                + "    $('.tree li:has(ul)').addClass('parent_li').find(' > span').attr('title', 'Collapse this branch');\n"
                + "   $('.tree li ul > li').hide();\n"
                + "    $('.tree li.parent_li > span').on('click', function (e) {\n"
                + "        var children = $(this).parent('li.parent_li').find(' > ul > li');\n"
                + "        if (children.is(\":visible\")) {\n"
                + "            children.hide('fast');\n"
                + "            $(this).attr('title', 'Expand this branch').find(' > i').addClass('icon-plus-sign').removeClass('icon-minus-sign');\n"
                + "        } else {\n"
                + "            children.show('fast');\n"
                + "            $(this).attr('title', 'Collapse this branch').find(' > i').addClass('icon-minus-sign').removeClass('icon-plus-sign');\n"
                + "        }\n"
                + "        e.stopPropagation();\n"
                + "    });\n"
                + "});\n"
                + "</script>\n"
                + "</head>");

        report.append("<body>\n"
                + "<div class=\"panel panel-primary\">"
                + "<div class=\"panel-heading\">\n"
                + "<h2 class=\"panel-title\">Bamboo Test Execution Log</h2>\n" //header of the web page
                + "</div>");

        StringBuilder x = iterator(source_list);
        report.append(x); // append iterative test_data variable in to report report variable

        report.append(
                "</div></body>\n"
                + "<div class=\"panel-footer\">\n"
                + "@ebuilder.com"
                + "</div>"
                + "</html>");

        File file = new File("C:\\Users\\maleen\\Desktop\\ASA_161\\test.html");// give the location where

        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));
            bw.write(report.toString());
            bw.close();

            System.out.println("write to file");
        } catch (IOException ex) {
            Logger.getLogger(HtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//

    StringBuilder iterator(JSONArray source_list) {

        for (int a = 0; a < source_list.length(); a++) {
            System.out.println("main_stories.length()" + source_list.length());
            try {
                String host_url = "";
                String parallel_agent_number = "";
                String story_filter = "";

                JSONObject properties = source_list.getJSONArray(a).getJSONObject(0);   //where json arry used
                // 1st object of the myArray has propertity object 

               
                if(properties.has("host_url")){
                   host_url = (String) properties.get("host_url");  
                }
                if (properties.has("parallel_agent_number")) {
                    parallel_agent_number = (String) properties.get("parallel_agent_number");
                }
                if (properties.has("story_filter")) {
                    story_filter = (String) properties.get("story_filter");
                }
                test_data.append("<div class=\"panel-body\"><div class=\"tree well\">\n");
                test_data.append(
                        "<div class=\"alert alert-error\">\n"
                        + "<ul><Strong>Host Url              :</Strong>" + host_url + "\n</ul>"
                        + "<ul><Strong>Parallel agent number :</Strong>" + parallel_agent_number + "</ul>"
                        + "<ul><Strong>Story Filter          :</Strong>" + story_filter + "</ul>"
                        + "</div>");

                test_data.append(" <ul>\n");

                numOfMainStories = source_list.getJSONArray(a).length();  /////
                for (int k = 1; k < numOfMainStories; k++) {       // because first element object of the array is property object 
                    //this loop starts with k=1

                    main_story = source_list.getJSONArray(a).getJSONObject(k);  // main_story
                    String main_story_name = (String) main_story.get("main_story_name");

                    if (true == (boolean) main_story.getBoolean("error_status")) {

                        // output The Main story 
                        test_data.append("<li><span class=\"badge badge-warning\"><i class=\"icon-folder-open\"></i>Main story " + (k + 1) + "</span> <a href=\"\">" + main_story_name + "</a><ul>");
                    } else {
                        System.out.println("primary");
                        test_data.append("<li><span class=\"label label-success\"><i class=\"icon-folder-open\"></i>Main story " + (k + 1) + "</span> <a href=\"\">" + main_story_name + "</a><ul>");
                    }
                    base_stories = (JSONArray) main_story.get("base_stories");  //base_stories
                    numOfBaseStories = base_stories.length();
                    for (int j = 1; j < numOfBaseStories; j++) {   //because the first element of the array is null 

                        String base_story = "";
                        boolean error_state = false;

                        //numOfSteps
                        numOfSteps = base_stories.getJSONArray(j).length();
                        for (int i = 0; i < numOfSteps; i++) {        //
                            step = base_stories.getJSONArray(j).getJSONObject(i);
                            step_name = step.getString("step_name");
                            execution_time = step.getString("execution_time");

                            if (step.has("screenshot_path")) {
                                screenshot_path = step.getString("screenshot_path");
                            }
                            if (step.has("error_name")) {
                                error_name = step.getString("error_name");
                                error_state = true;
                                base_story += "<li><span class=\"badge badge-warning\"> step " + (i + 1)
                                        + "</span><a href=\"\"> " + step_name + "</a><span class=\"badge\">" + execution_time + "</span>\n <ul>"
                                        + "<li><span class=\"badge badge-warning\">Error</span><div class=\"alert alert-error\">" + error_name
                                        + "</div></li><li><span class=\"badge badge-warning\">Screenshot</span>"
                                        + "<div class=\"alert alert-error\">" + screenshot_path + "</div></li></ul></li>";

                            } else {
                                base_story += "<li><table><tr class=\"table-danger\"><td><span class=\"label label-success\">"
                                        + " step " + (i + 1) + "</span>"
                                        + " <a href=\"\"> " + step_name + "</a></td><td>"
                                        + "<span class=\"badge\">" + execution_time + "</span></td></tr></table></li>\n";
                            }
                        }
                        if (error_state) {
                            test_data.append("<li><span class=\"badge badge-warning\"><i class=\"icon-minus-sign\"></i>Base Story " + j + "</span> <a href=\"\">Start Base Story " + j + "</a>\n<ul>\n");
                        } else {
                            test_data.append("<li><span class=\"label label-info\"><i class=\"icon-minus-sign\"></i>Base Story " + j + "</span> <a href=\"\">Start Base Story " + j + "</a>\n<ul>\n");
                        }
                        test_data.append(base_story);
                        test_data.append("</ul></li>\n");
                        error_state = false;

                    }
                    test_data.append("</ul></li>\n");
                }
            } catch (JSONException ex) {
                Logger.getLogger(HtmlConverter.class.getName()).log(Level.SEVERE, null, ex);
            }
            test_data.append(
                    "</ul>\n"
                    + "</div>"
                    + "</div>");

        }   //loop end  
        return test_data;

    }

}
