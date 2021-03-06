////////////////////////////////////////////////////////////////////////
//
//     Copyright (c) 2009-2014 Denim Group, Ltd.
//
//     The contents of this file are subject to the Mozilla Public License
//     Version 2.0 (the "License"); you may not use this file except in
//     compliance with the License. You may obtain a copy of the License at
//     http://www.mozilla.org/MPL/
//
//     Software distributed under the License is distributed on an "AS IS"
//     basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
//     License for the specific language governing rights and limitations
//     under the License.
//
//     The Original Code is ThreadFix.
//
//     The Initial Developer of the Original Code is Denim Group, Ltd.
//     Portions created by Denim Group, Ltd. are Copyright (C)
//     Denim Group, Ltd. All Rights Reserved.
//
//     Contributor(s): Denim Group, Ltd.
//
////////////////////////////////////////////////////////////////////////
package com.denimgroup.threadfix.importer.utils;

import com.denimgroup.threadfix.data.entities.Scan;
import com.denimgroup.threadfix.importer.ScanLocationManager;
import com.denimgroup.threadfix.importer.cli.ScanParser;
import com.denimgroup.threadfix.importer.config.SpringConfiguration;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by mac on 8/18/14.
 */
@Component
public class ParserUtils {

    private ParserUtils(){}

    /**
     *
     * @param filePath path from scan file repository root
     * @return parsed Scan (no linked Vulnerability objects)
     */
    public static Scan getScan(String filePath) {
        String fullPath = ScanLocationManager.getRoot() + filePath;

        File scanFile = new File(fullPath);

        assert scanFile.exists() && scanFile.isFile() :
            "Invalid file path: " + fullPath + " passed to getScan().";

        return SpringConfiguration
                .getContext()
                .getBean(ScanParser.class)
                .getScan(fullPath);
    }

}
