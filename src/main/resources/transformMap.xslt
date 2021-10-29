<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:map="http://www.w3.org/2005/xpath-functions/map"
    xmlns:array="http://www.w3.org/2005/xpath-functions/array"
    exclude-result-prefixes="#all"
    version="3.0">

  <xsl:param name="exMap" as="map(xs:string, xs:string)" />
  
  <xsl:variable name="address" select="$exMap/entry[@key='1']" />
  
  <xsl:mode on-no-match="shallow-copy"/>

  <xsl:template match="/">
        <rentalProperties>
            <xsl:for-each select = "rentalProperties/property"> 
            <property>              
                <xsl:attribute name="contact"><xsl:value-of select='@contact'/></xsl:attribute>    
                <type><xsl:value-of select="type"/></type>
                <price><xsl:value-of select="price"/></price>
                <numberOfBedrooms><xsl:value-of select="numberOfBedrooms"/></numberOfBedrooms>
                <numberOfBathrooms><xsl:value-of select="numberOfBathrooms"/></numberOfBathrooms>
                <garage><xsl:value-of select="garage"/></garage> 
                <greeting><xsl:value-of select="$address"/></greeting>   
            </property>
           </xsl:for-each>    
        </rentalProperties>    
    </xsl:template>
 </xsl:stylesheet>