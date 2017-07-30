package org.citic.iiot.persist.gener.metadata;

public class ForeginKey {

	/* 引用的外键列所在表的名称 */
	private String FKTable;

	/* 引用的外键列的名称 */
	private String FKColumn;

	/* 主键列所在表的名称 */
	private String PKTable;

	/* 主键列的名称 */
	private String PKColumn;

	public String getFKTable() {
		return FKTable;
	}

	public void setFKTable(String fKTable) {
		FKTable = fKTable;
	}

	public String getFKColumn() {
		return FKColumn;
	}

	public void setFKColumn(String fKColumn) {
		FKColumn = fKColumn;
	}

	public String getPKTable() {
		return PKTable;
	}

	public void setPKTable(String pKTable) {
		PKTable = pKTable;
	}

	public String getPKColumn() {
		return PKColumn;
	}

	public void setPKColumn(String pKColumn) {
		PKColumn = pKColumn;
	}

}
