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
package com.denimgroup.threadfix.service;

import com.denimgroup.threadfix.data.dao.FilterJsonBlobDao;
import com.denimgroup.threadfix.data.dao.GenericNamedObjectDao;
import com.denimgroup.threadfix.data.entities.FilterJsonBlob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by mac on 5/13/14.
 */
@Service
public class FilterJsonBlobServiceImpl extends AbstractNamedObjectService<FilterJsonBlob> implements FilterJsonBlobService {

    @Autowired
    private FilterJsonBlobDao filterJsonBlobDao;

    @Override
    public GenericNamedObjectDao<FilterJsonBlob> getDao() {
        return filterJsonBlobDao;
    }

    @Override
    public int updateDefaultTrendingFilter() {
        return filterJsonBlobDao.updateDefaultTrendingFilter();
    }
}
