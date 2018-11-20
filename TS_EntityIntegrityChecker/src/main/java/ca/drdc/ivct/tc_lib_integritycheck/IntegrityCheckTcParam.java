/*******************************************************************************
 * Copyright (C) Her Majesty the Queen in Right of Canada, 
 * as represented by the Minister of National Defence, 2018
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
 *******************************************************************************/
package ca.drdc.ivct.tc_lib_integritycheck;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import de.fraunhofer.iosb.tc_lib.IVCT_TcParam;
import de.fraunhofer.iosb.tc_lib.TcInconclusive;

/**
 * Store test case parameters
 *
 * @author mlavallee
 */
public class IntegrityCheckTcParam implements IVCT_TcParam {
    
    // Get test case parameters
    private String federationName;
    
    private String rtiHost;
    private String rtiPort;

    private URL[] fomUrls;
    private URL[] fadUrls;

    private String settingsDesignator;


    public IntegrityCheckTcParam(final String paramJson) throws TcInconclusive {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;
        try {
            jsonObject = (JSONObject) jsonParser.parse(paramJson);

            federationName = (String) jsonObject.get("federationName");
            if (federationName == null) {
                throw new TcInconclusive("The key federationName was not found");
            }

            rtiHost = (String) jsonObject.get("rtiHostName");
            if (rtiHost == null) {
                throw new TcInconclusive("The key rtiHostName was not found");
            }
            rtiPort = (String) jsonObject.get("rtiPort");
            if (rtiPort == null) {
                throw new TcInconclusive("The rti port id was not found");
            }
            this.settingsDesignator = "crcAddress=" + this.rtiHost + ":" + this.rtiPort;

            // get FOM files list from the JSON object
            JSONArray fadArray = (JSONArray) jsonObject.get("fadFiles");
            if (fadArray == null) {
                throw new TcInconclusive("The key fadFiles was not found");
            } else {
                this.fadUrls = jsonArrayToUrlList(fadArray);
            }

            // get FOM files list from the JSON object
            JSONArray fomArray = (JSONArray) jsonObject.get("fomFiles");
            if (fomArray == null) {
                throw new TcInconclusive("The key fomFiles was not found");
            } else {
                this.fomUrls = jsonArrayToUrlList(fomArray);
            }

        } catch (ParseException e) {
            throw new TcInconclusive("Invalid configuration file", e);
        }
    }

    public URL[] jsonArrayToUrlList(JSONArray jsonArray) throws TcInconclusive {
        int index = 0;
        URL[] urls = new URL[jsonArray.size()];
        Iterator<?> iter = jsonArray.iterator();
        while (iter.hasNext()) {
            JSONObject element = (JSONObject) iter.next();
            String fileName = (String) element.get("fileName");
            // add FOM file in url array
            try {
                URI uri = (new File(fileName)).toURI();
                urls[index++] = uri.toURL();
            } catch (MalformedURLException e) {
                throw new TcInconclusive("Could not open the fom file :" + fileName, e);
            }
        }
        return urls;
    }

    /**
     * @return the RTI host value
     */
    public String getRtiHost() {
        return this.rtiHost;
    }

    /**
     * @return the federation name
     */
    @Override
    public String getFederationName() {
        return this.federationName;
    }

    /**
     * @return the settings designator
     */
    @Override
    public String getSettingsDesignator() {
        return this.settingsDesignator;
    }

    /**
     * @return the fom urls
     */
    @Override
    public URL[] getUrls() {
        return this.fomUrls;
    }

    public URL[] getFadUrls() {
        return fadUrls;
    }
}