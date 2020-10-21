package com.clueride.imp.gpx;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;

public class ParseGPX {
//    @Inject
//    private Logger LOGGER;

    private Track track = null;

    public ParseGPX() {
    }

    public Track getTrackFromGPX(String gpx) {
        if (gpx != null && gpx.length() != 0) {
            if (this.track == null) {
                this.track = new Track();
            } else {
                this.track.clear();
            }

            XMLReader producer;
            try {
                producer = XMLReaderFactory.createXMLReader();
            } catch (SAXException var9) {
                System.err.println("Can't get parser, check configuration: " + var9.getMessage());
                return null;
            }

            try {
                DefaultHandler consumer = new ParseGPX.GPXHandler();
                producer.setContentHandler(consumer);
                producer.setErrorHandler(consumer);
            } catch (Exception var8) {
                System.err.println("Can't set up consumers:" + var8.getMessage());
                return null;
            }

            StringReader stringReader = new StringReader(gpx);

            try {
                producer.parse(new InputSource(stringReader));
            } catch (IOException | SAXException var6) {
                var6.printStackTrace();
            }

            return this.track;
        } else {
            return null;
        }
    }

    public class GPXHandler extends DefaultHandler {
        private boolean eleOpen = false;
        private boolean urlOpen = false;
        private boolean displayNameOpen = false;
        Trackpoint trkPt;

        public GPXHandler() {
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            if (this.eleOpen) {
                super.characters(ch, start, length);
                String eleValue = new String(ch, start, length);
                if (eleValue.length() > 0) {
                    try {
                        this.trkPt.setAltitude(Double.parseDouble(eleValue));
                    } catch (NumberFormatException var5) {
//                        LOGGER.warn("Unable to parse Elevation Value >" + this.eleValue + "< as a Double");
                    }
                }

//                LOGGER.debug(this.trkPt.toString());
                this.eleOpen = false;
            }

            String displayNameValue;
            if (this.urlOpen) {
                super.characters(ch, start, length);
                displayNameValue = new String(ch, start, length);
                ParseGPX.this.track.setSourceUrl(displayNameValue);
//                LOGGER.debug("Source URL: " + displayNameValue);
                this.urlOpen = false;
            }

            if (this.displayNameOpen) {
                super.characters(ch, start, length);
                displayNameValue = new String(ch, start, length);
                ParseGPX.this.track.setDisplayName(displayNameValue);
//                LOGGER.debug("Display Name: " + displayNameValue);
                this.displayNameOpen = false;
            }

        }

        public void startElement(String arg0, String arg1, String arg2, Attributes arg3) throws SAXException {
            super.startElement(arg0, arg1, arg2, arg3);
            if (arg1.equals("trkpt")) {
                HashMap<String, String> map = new HashMap<>();

                for(int i = 0; i < arg3.getLength(); ++i) {
                    map.put(arg3.getQName(i), arg3.getValue(i));
                }

                this.trkPt = new Trackpoint(new Double((String)map.get("lat")), new Double((String)map.get("lon")));
                ParseGPX.this.track.addTrackpoint(this.trkPt);
            }

            if (arg1.equals("ele")) {
                this.eleOpen = true;
            }

            if (arg1.equals("url")) {
                this.urlOpen = true;
            }

            if (arg1.equals("name")) {
                this.displayNameOpen = true;
            }

        }
    }
}
