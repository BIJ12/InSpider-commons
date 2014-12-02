<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:param name="doctype-system"/>

    <xsl:template match="/">
        <xsl:text disable-output-escaping='yes'>&#xa;&lt;!DOCTYPE WMT_MS_Capabilities SYSTEM "</xsl:text>
        <xsl:value-of select="$doctype-system"/>
        <xsl:text disable-output-escaping='yes'>" [&lt;!ELEMENT VendorSpecificCapabilities EMPTY>]>&#xa;</xsl:text>
        <xsl:apply-templates/>        
    </xsl:template>
    
    <xsl:template match="Layer[Layer]">
        <xsl:copy>            
            <xsl:copy-of select="attribute::*"/>            
            <xsl:apply-templates select="*[not(self::Layer)]"/>            
            <xsl:for-each select="Layer">                
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