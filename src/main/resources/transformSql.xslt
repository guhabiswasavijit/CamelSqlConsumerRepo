<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:output method="text"/>
  
  <xsl:template match="/">
  	   <xsl:variable name="v_TableName"><xsl:value-of select="TableMetaDataBean/tableName"/></xsl:variable>
       CREATE TABLE <xsl:value-of select="$v_TableName"/> (id INT NOT NULL AUTO_INCREMENT,PRIMARY KEY (id));
       <xsl:for-each select = "TableMetaDataBean/sqlColumns/sqlColumns">
          <xsl:choose> 
	          <xsl:when test="columnType='VARCHAR'"> 
	            ALTER TABLE <xsl:value-of select="$v_TableName"/> ADD <xsl:value-of select="columnName"/> <xsl:value-of select="columnType"/>(100);
	          </xsl:when>	
	          <xsl:otherwise> 
	            ALTER TABLE <xsl:value-of select="$v_TableName"/> ADD <xsl:value-of select="columnName"/> <xsl:value-of select="columnType"/>; 
	          </xsl:otherwise> 
	       </xsl:choose>          
      </xsl:for-each>    
     </xsl:template>
 </xsl:stylesheet>