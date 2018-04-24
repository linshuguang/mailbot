<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >

    <xsl:output indent="yes" method="xml"/>


    <xsl:template match="/" >
        <beans xmlns="http://www.springframework.org/schema/beans"
               xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
               xmlns:context="http://www.springframework.org/schema/context"
               xsi:schemaLocation="http://www.springframework.org/schema/beans
                http://www.springframework.org/schema/beans/spring-beans-3.0.xsd
                http://www.springframework.org/schema/context
                http://www.springframework.org/schema/context/spring-context.xsd">
            <context:annotation-config/>
            <!--context:property-placeholder location="classpath:/context.properties"  system-properties-mode="FALLBACK"/-->
            <!--context:property-placeholder location="classpath*:context-${{prop}}.properties"  system-properties-mode="FALLBACK"/-->
            <!--context:property-placeholder location="classpath*:context.properties"  system-properties-mode="FALLBACK"/-->
            <!--context:property-placeholder location="file:${{prop}}"  system-properties-mode="FALLBACK"/-->

            <bean id="springContextHelper" class="com.me.utils.BeanLoader"></bean>
            <context:component-scan base-package="com.me">
            </context:component-scan>

            <!--xsl:import href="conf/test.xsl"/-->
            <!--xsl:value-of select="document('conf/context.xml')"/-->

            <xsl:apply-templates select="config"/>
            <!--bean id="defaultDS" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
                <property name="driverClassName" value="${{db.driver}}" />
                <property name="url" value="${{db.url}}" />
                <property name="username" value="${{db.user}}" />
                <property name="password" value="${{db.password}}" />
            </bean-->

        </beans>

    </xsl:template>


    <xsl:template match="config">



        <bean class="com.me.mail.EMail">
            <property name="subject" >
                <xsl:apply-templates select="subject"/>
            </property>

            <property name="recipient" >
                <xsl:apply-templates select="recipient"/>
            </property>

            <property name="content" >
                <xsl:apply-templates select="content"/>
            </property>
        </bean>

        <!--xsl:apply-templates select="resources"/>
        <xsl:apply-templates select="context"/-->


    </xsl:template>

    <xsl:template match="subject">
        <bean class="com.me.mail.Subject">
            <property name="prefix">
                <xsl:attribute name="value">
                    <xsl:value-of select="prefix" />
                </xsl:attribute>
            </property>
            <property name="title">
                <xsl:attribute name="value">
                    <xsl:value-of select="title" />
                </xsl:attribute>
            </property>
            <property name="postfix">
                <xsl:attribute name="value">
                    <xsl:value-of select="postfix" />
                </xsl:attribute>
            </property>
        </bean>
    </xsl:template>


    <xsl:template match="recipient">
        <bean class="com.me.mail.Recipient">
            <property name="recipient">
                <set>
                    <!--xsl:for-each select="email[@type='test']"-->
                    <xsl:for-each select="email">
                        <value>
                            <xsl:value-of select="." />
                        </value>

                    </xsl:for-each>
                </set>
            </property>
        </bean>
    </xsl:template>


    <xsl:template match="content">
        <bean class="com.me.mail.Content">
            <property name="records">
                <list>
                    <xsl:for-each select="record">

                        <bean class="com.me.mail.Record">
                            <property name="render">
                                <xsl:apply-templates select="view"/>
                            </property>
                            <property name="executor">
                                <xsl:apply-templates select="model"/>
                            </property>
                        </bean>
                        <!--xsl:apply-templates select="record"/-->
                    </xsl:for-each>
                </list>
            </property>
        </bean>
    </xsl:template>


    <xsl:template match="model">
        <xsl:choose>
            <xsl:when test="@type='file'">
                <bean class="com.me.executor.SQLFileExecutor">
                    <constructor-arg>
                        <xsl:copy-of select="value" />
                    </constructor-arg>
                    <xsl:if test="rKey">
                        <property name="rKey">
                            <value>
                                <xsl:apply-templates select="rKey"/>
                            </value>
                        </property>
                    </xsl:if>
                    <xsl:if test="transpose">
                        <property name="transpose">
                            <value>
                                <xsl:apply-templates select="transpose"/>
                            </value>
                        </property>
                    </xsl:if>
                </bean>
            </xsl:when>
            <xsl:when test="@type='sql'">
                <bean class="com.me.executor.SQLExecutor">
                    <constructor-arg>
                        <xsl:copy-of select="value" />
                    </constructor-arg>
                    <xsl:if test="rKey">
                        <property name="rKey">
                            <value>
                                <xsl:apply-templates select="rKey"/>
                            </value>
                        </property>
                    </xsl:if>
                    <xsl:if test="transpose">
                        <property name="transpose">
                            <value>
                                <xsl:apply-templates select="transpose"/>
                            </value>
                        </property>
                    </xsl:if>
                </bean>
            </xsl:when>
            <xsl:when test="@type='text'">
                <bean class="com.me.executor.EchoExecutor">
                    <constructor-arg>
                        <xsl:copy-of select="value" />
                    </constructor-arg>
                    <!--xsl:if test="rKey">
                        <property name="rKey">
                            <value>
                                <xsl:apply-templates select="rKey"/>
                            </value>
                        </property>
                    </xsl:if-->
                </bean>
            </xsl:when>
            <xsl:otherwise>
                <null />
            </xsl:otherwise>
        </xsl:choose>

    </xsl:template>




    <xsl:template match="view">

        <xsl:choose>
            <xsl:when test="@type='html'">
                <bean class="com.me.render.HtmlTable">
                    <xsl:if test="caption">
                        <property name="caption">
                            <value>
                                <xsl:apply-templates select="caption"/>
                            </value>
                        </property>
                    </xsl:if>
                </bean>
            </xsl:when>
            <xsl:when test="@type='echo'">
                <bean class="com.me.render.EchoRender">
                </bean>
            </xsl:when>
            <xsl:when test="@type='pie'">
                <bean class="com.me.render.PieRender">
                </bean>
            </xsl:when>
        </xsl:choose>
    </xsl:template>


    <!--xsl:template match="record">
        <bean clacom.me.back.mailmail.Record">
        </bean>
    </xsl:template-->


    <!--xsl:template match="resources">
        <bean class="com.me.resource.ResourceManager">
            <property name="resourceMap" >
                <map>
                    <xsl:for-each select="resource">
                        <entry>
                            <xsl:attribute name="key">
                                <xsl:value-of select="@rid" />
                            </xsl:attribute>

                            <xsl:choose>
                                <xsl:when test="@type='db'">
                                    <bean class="com.me.resource.DBResource">
                                        <constructor-arg>
                                            <bean class="com.mchange.v2.c3p0.ComboPooledDataSource" destroy-method="close">
                                                <property name="driverClass">
                                                    <value>
                                                        <xsl:apply-templates select="driver"/>
                                                    </value>
                                                </property>
                                                <property name="jdbcUrl" >
                                                    <value>
                                                        <xsl:apply-templates select="host"/>
                                                    </value>
                                                </property>
                                                <property name="user" >
                                                    <value>
                                                        <xsl:apply-templates select="user"/>
                                                    </value>
                                                </property>
                                                <property name="password" >
                                                    <value>
                                                        <xsl:apply-templates select="password"/>
                                                    </value>
                                                </property>
                                                <property name="minPoolSize" value="1" />
                                                <property name="initialPoolSize" value="1" />
                                                <property name="maxPoolSize" value="2" />
                                                <property name="acquireIncrement" value="1" />
                                                <property name="maxIdleTime" value="3600" />
                                                <property name="testConnectionOnCheckout" value="false" />
                                                <property name="preferredTestQuery" value="select 1" />
                                                <property name="idleConnectionTestPeriod" value="1800" />
                                            </bean>
                                        </constructor-arg>
                                    </bean>
                                </xsl:when>
                                <xsl:otherwise>
                                    <null />
                                </xsl:otherwise>
                            </xsl:choose>
                        </entry>
                    </xsl:for-each>
                </map>
            </property>

            <xsl:if test="@defaultResource">
                <property name="defaultResource" >
                    <value>
                        <xsl:value-of select="@defaultResource" />
                    </value>
                </property>
            </xsl:if>


        </bean>
    </xsl:template>



    <xsl:template match="context">
        <bean class="com.me.context.Context">
            <property name="templatePath" >
                <value>
                    <xsl:apply-templates select="templatePath"/>
                </value>
            </property>
            <property name="magicPath" >
                <value>
                    <xsl:apply-templates select="magicPath"/>
                </value>
            </property>
            <property name="magic2Path" >
                <value>
                    <xsl:apply-templates select="magic2Path"/>
                </value>
            </property>
            <property name="workingPath" >
                <value>
                    <xsl:apply-templates select="workingPath"/>
                </value>
            </property>


        </bean>
    </xsl:template-->

</xsl:stylesheet>
