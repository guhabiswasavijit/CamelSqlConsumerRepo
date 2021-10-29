<?xml version="1.0" encoding="UTF-8" ?>
<xsl:stylesheet version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output omit-xml-declaration="yes" indent="yes"/>
    <xsl:output method="text"/>
    <xsl:strip-space elements="columnName" />
  
  <xsl:template match="/">
  	   <xsl:variable name="v_TableName"><xsl:value-of select="TableMetaDataBean/tableName"/></xsl:variable>
  	   
       INSERT TABLE <xsl:value-of select="$v_TableName"/> (
           <xsl:for-each select = "TableMetaDataBean/sqlColumns/sqlColumns">
                 <xsl:variable name="CurrentVenueKeyValue" select="normalize-space(@name)"/>
	             <xsl:choose>
		                <xsl:when test="position() = last()">
		                    <xsl:value-of select="normalize-space(columnName)"/>)
		                </xsl:when>
		                <xsl:otherwise> 
				             <xsl:value-of select="normalize-space(columnName)"/>, 
				        </xsl:otherwise>
			      </xsl:choose>
           </xsl:for-each>
           VALUES(
       	  <xsl:for-each select = "TableMetaDataBean/sqlColumns/sqlColumns">
       	     <xsl:choose>
       	          <xsl:when test="columnType='VARCHAR'">
       	               <xsl:choose>
		                    <xsl:when test="position() = last()">
		                    	'$<xsl:value-of select="normalize-space(columnName)"/>')
		                	</xsl:when>
		                	<xsl:otherwise> 
				             	'$<xsl:value-of select="normalize-space(columnName)"/>', 
				        	</xsl:otherwise>
		               </xsl:choose>
		           </xsl:when>
		            <xsl:otherwise> 
		                 <xsl:choose>
				             <xsl:when test="position() = last()">
		                    	$<xsl:value-of select="normalize-space(columnName)"/>)
		                	</xsl:when>
		               		<xsl:otherwise> 
				             	$<xsl:value-of select="normalize-space(columnName)"/>, 
				        	</xsl:otherwise>
				         </xsl:choose>				    
				    </xsl:otherwise>
		      </xsl:choose>
         </xsl:for-each>
      )   
     </xsl:template>
 </xsl:stylesheet>