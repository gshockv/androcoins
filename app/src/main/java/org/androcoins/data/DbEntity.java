package org.androcoins.data;

import android.content.ContentValues;

/**
 * Persistable database entity
 * @author vitaly gashock
 */

public interface DbEntity {
	public String getTableName();
	public long getId();
	public ContentValues prepare();
}
