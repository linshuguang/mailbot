package com.me.service;

import com.me.enums.RunModeEnums;
import org.apache.commons.cli.*;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by kenya on 2017/11/24.
 */
public class MainApp {

    public static final String DEFAULT_UID      ="kenya";
    public static final String DEFAULT_XMLPATH  =null;
    public static final String DEFAULT_XSLPATH  ="mail.xsl";
    public static final String DEFAULT_XSDPATH  ="mail.xsd";
    public static final String DEFAULT_WORKING_PATH = System.getProperty("user.dir");
    public static final int    DEFAULT_PORT     =8096;
    public static final RunModeEnums DEFAULT_RUNMODE = RunModeEnums.CONSOLE;

    private static final Logger LOGGER = LoggerFactory.getLogger(MainApp.class);

    public static class NullPrintStream extends PrintStream {

        public NullPrintStream() {
            super(new NullByteArrayOutputStream());
        }

        private static class NullByteArrayOutputStream extends ByteArrayOutputStream {

            @Override
            public void write(int b) {
                // do nothing
            }

            @Override
            public void write(byte[] b, int off, int len) {
                // do nothing
            }

            @Override
            public void writeTo(OutputStream out) throws IOException {
                // do nothing
            }

        }

    }


    public static void main(String[] args) {



        String uid      = DEFAULT_UID;
        String xslPath  = DEFAULT_XSLPATH;
        String xsdPath  = DEFAULT_XSDPATH;
        String xmlPath  = DEFAULT_XMLPATH;
        String workingPath  = DEFAULT_WORKING_PATH;
        String output   = null;
        int port = DEFAULT_PORT;
        RunModeEnums mode  = DEFAULT_RUNMODE;
        boolean isDeliver = true;
        boolean silent = true;



        CommandLine commandLine;
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();

        options.addOption(Option.builder("l").argName("xsl").hasArg().desc("The l option").build());
        options.addOption(Option.builder("d").argName("xsd").hasArg().desc("The d option").build());
        options.addOption(Option.builder("x").argName("xml").hasArg().desc("The x option").build());
        options.addOption(Option.builder("m").argName("mode").hasArg().desc("The m option").build());
        options.addOption(Option.builder("o").argName("out").hasArg().desc("The o option").build());
        options.addOption(Option.builder("u").argName("uid").hasArg().desc("The u option").build());
        options.addOption(Option.builder("p").argName("port").hasArg().desc("The p option").build());
        options.addOption(Option.builder("w").argName("work").hasArg().desc("The w option").build());
        options.addOption(Option.builder().longOpt("deliver").desc("The deliver option").build());
        options.addOption(Option.builder().longOpt("silent").desc("The silent option").build());


        try
        {
            commandLine = parser.parse(options, args);

            if (commandLine.hasOption("l")){
                xslPath = commandLine.getOptionValue("l");
            }else if (commandLine.hasOption("d")){
                xsdPath = commandLine.getOptionValue("d");
            }else if (commandLine.hasOption("x")){
                xmlPath = commandLine.getOptionValue("x");
            }else if (commandLine.hasOption("m")){
                mode = RunModeEnums.getByName(commandLine.getOptionValue("m"));
            }else if (commandLine.hasOption("o")){
                output = commandLine.getOptionValue("o");
            }else if (commandLine.hasOption("silent")){
                LOGGER.debug("silent");
                silent = true;
            }else if (commandLine.hasOption("u")){
                uid = commandLine.getOptionValue("u");
            }else if (commandLine.hasOption("p")){
                port = Integer.parseInt(commandLine.getOptionValue("p"));
            }else if (commandLine.hasOption("deliver")){
                LOGGER.debug("deliver");
                isDeliver = true;
            }else if (commandLine.hasOption("w")){
                workingPath = commandLine.getOptionValue("w");
            }
            //return;
        }catch (ParseException exception){
            LOGGER.error("Parse error: {}",exception.getMessage());
            String header = "[<arg1> [<arg2> [<arg3> ...\n       Options, flags and arguments may be in any order";
            String footer = "This is DwB's solution brought to Commons CLI 1.3.1 compliance (deprecated methods replaced)";
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("CLIsample", header, options, footer, true);
            return;
        }

        if(silent){
            System.setOut(new MainApp.NullPrintStream());
        }

        try {
            if (mode == RunModeEnums.CONSOLE) {
                OutputStream outputStream = System.out;
                if (output != null) {
                    if(!"stdout".equals(output)) {
                        outputStream = new FileOutputStream(new File(output));
                    }
                }
                ConsoleMain consoleMain = new ConsoleMain();

                consoleMain.serve(uid, xmlPath, xslPath, xsdPath,
                        outputStream, isDeliver);
            }else if (mode == RunModeEnums.SERVICE) {

                ServiceMain serviceMain = new ServiceMain();
                serviceMain.serve(port);
            }else if (mode == RunModeEnums.CLIENT) {
                OutputStream outputStream = System.out;
                if (output != null) {
                    outputStream = new FileOutputStream(new File(output));
                }
                ClientMain clientMain = new ClientMain();
                clientMain.serve(workingPath, xmlPath, outputStream);
            }
        }catch(Exception e){
            LOGGER.error(ExceptionUtils.getStackTrace(e));
        }
    }
}
