/*
 * Author: Andliage Pox
 * Date: 2020-06-01
 */

package com.andliage.library.dao;

import com.andliage.library.entity.OPLog;
import org.springframework.stereotype.Repository;

@Repository
public class OPLogDAO extends BaseDAO {
    public void saveOPLog(OPLog log) {
        template.save(log);
        template.update(log.getAdmin());
        template.flush();
    }
}
