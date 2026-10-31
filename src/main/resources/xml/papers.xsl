<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8"/>
    <xsl:strip-space elements="*"/>

    <xsl:key name="papers-by-type" match="Paper" use="Type"/>

    <xsl:template match="/Papers">
        <PapersByType>
            <xsl:for-each select="Paper[generate-id() = generate-id(key('papers-by-type', Type)[1])]">
                <xsl:sort select="Type"/>
                <Group type="{Type}">
                    <xsl:apply-templates select="key('papers-by-type', Type)"/>
                </Group>
            </xsl:for-each>
        </PapersByType>
    </xsl:template>

    <xsl:template match="*">
        <xsl:element name="{name()}">
            <xsl:copy-of select="@*"/>
            <xsl:apply-templates/>
        </xsl:element>
    </xsl:template>

</xsl:stylesheet>