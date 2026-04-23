package com.example.erp.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/** Minimal stdin / stdout helper used by every module CLI. */
public final class Cli {

    private final BufferedReader in;
    private final PrintStream out;

    public Cli() {
        this(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), System.out);
    }

    public Cli(BufferedReader in, PrintStream out) {
        this.in = in;
        this.out = out;
    }

    public void println() { out.println(); }
    public void println(String s) { out.println(s); }
    public void printf(String fmt, Object... args) { out.printf(fmt, args); }

    public String prompt(String label) {
        out.print(label);
        try {
            var line = in.readLine();
            return line == null ? "" : line.trim();
        } catch (Exception e) {
            return "";
        }
    }
}
