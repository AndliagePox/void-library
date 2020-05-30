/*
 * Author: Andliage Pox
 * Date: 2020-05-30
 */

package com.andliage.library.dao;

import com.andliage.library.entity.BRLog;
import org.springframework.stereotype.Repository;

@Repository
public class BRLogDAO extends BaseDAO {
    public void saveBRLog(BRLog log) {
        template.save(log);
        template.flush();
    }
}
