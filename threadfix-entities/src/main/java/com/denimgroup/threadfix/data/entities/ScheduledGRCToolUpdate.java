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

package com.denimgroup.threadfix.data.entities;

/**
 * Created by zabdisubhan on 8/14/14.
 */

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="ScheduledGRCToolUpdate")
public class ScheduledGRCToolUpdate extends ScheduledJob {

    private static final long serialVersionUID = 1223869621339558275L;

    public static ScheduledGRCToolUpdate getDefaultScheduledUpdate(){

        ScheduledGRCToolUpdate defaultScheduledUpdate = new ScheduledGRCToolUpdate();

        defaultScheduledUpdate.setFrequency(ScheduledFrequencyType.DAILY.getDescription());
        defaultScheduledUpdate.setHour(6);
        defaultScheduledUpdate.setMinute(0);
        defaultScheduledUpdate.setPeriod("AM");
        defaultScheduledUpdate.setDay(null);

        return defaultScheduledUpdate;
    }

}
