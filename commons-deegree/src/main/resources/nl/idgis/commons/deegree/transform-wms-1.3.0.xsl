<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  xmlns:wms="http://www.opengis.net/wms" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    
    <xsl:template match="wms:Layer[wms:Layer]">
        <xsl:copy>            
            <xsl:copy-of select="attribute::*"/>            
            <xsl:apply-templates select="*[not(self::wms:Layer)]"/>            
            <xsl:for-each select="wms:Layer">                
                <xsl:sort data-type="number" select="position()" order="descending"/>                                    
                <xsl:copy>
                    <xsl:copy-of select="attribute::*"/>
                    <xsl:apply-templates/>
                </xsl:copy>
            </xsl:for-each>
        </xsl:copy>
    </xsl:template>
    
    <xsl:template match="*">
        <xsl:copy>
            <xsl:copy-of select="attribute::*"/>
            <xsl:apply-templates/>
        </xsl:copy>
    </xsl:template>
</xsl:stylesheet>