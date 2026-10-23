package com.lumi2021.urna.application;

public class BallotBox {

    public String emitBulletin(){
        var sb = new StringBuilder();
        var width = 40;

        sb.append(TextAlign.center("Justiça Eleitoral", width)+ "\n");
        sb.append(TextAlign.center("Tribunal Regional Eleitoral [ZZ]", width)+"\n");
        sb.append("=".repeat(width)+"\n");



        return sb.toString();

    }


    private final class TextAlign {
        private TextAlign() {}

        public static String left(String text, int width) {
            text = text == null ? "null" : text;
            if (text.length() >= width) return text;
            return text + " ".repeat(width - text.length());
        }

        public static String right(String text, int width) {
            text = text == null ? "null" : text;
            if (text.length() >= width) return text;
            return " ".repeat(width - text.length()) + text;
        }

        public static String center(String text, int width) {
            text = text == null ? "null" : text;
            if (text.length() >= width) return text;

            int totalPadding = width - text.length();
            int leftPadding = totalPadding / 2;
            int rightPadding = totalPadding - leftPadding;

            return " ".repeat(leftPadding)
                    + text
                    + " ".repeat(rightPadding);
        }

        public static String pad(String text, int width) {
            return left(text, width);
        }

        public static String truncate(String text, int width) {
            text = text == null ? "null" : text;
            if (width <= 0) return "";
            if (text.length() <= width) return text;
            return text.substring(0, width);
        }
    }
}