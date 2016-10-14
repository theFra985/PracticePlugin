/*
 * Decompiled with CFR 0_118.
 */
package com.google.gson.stream;

import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

public class JsonReader
implements Closeable {
    private static final char[] NON_EXECUTE_PREFIX = ")]}'\n".toCharArray();
    private static final long MIN_INCOMPLETE_INTEGER = -922337203685477580L;
    private static final int PEEKED_NONE = 0;
    private static final int PEEKED_BEGIN_OBJECT = 1;
    private static final int PEEKED_END_OBJECT = 2;
    private static final int PEEKED_BEGIN_ARRAY = 3;
    private static final int PEEKED_END_ARRAY = 4;
    private static final int PEEKED_TRUE = 5;
    private static final int PEEKED_FALSE = 6;
    private static final int PEEKED_NULL = 7;
    private static final int PEEKED_SINGLE_QUOTED = 8;
    private static final int PEEKED_DOUBLE_QUOTED = 9;
    private static final int PEEKED_UNQUOTED = 10;
    private static final int PEEKED_BUFFERED = 11;
    private static final int PEEKED_SINGLE_QUOTED_NAME = 12;
    private static final int PEEKED_DOUBLE_QUOTED_NAME = 13;
    private static final int PEEKED_UNQUOTED_NAME = 14;
    private static final int PEEKED_LONG = 15;
    private static final int PEEKED_NUMBER = 16;
    private static final int PEEKED_EOF = 17;
    private static final int NUMBER_CHAR_NONE = 0;
    private static final int NUMBER_CHAR_SIGN = 1;
    private static final int NUMBER_CHAR_DIGIT = 2;
    private static final int NUMBER_CHAR_DECIMAL = 3;
    private static final int NUMBER_CHAR_FRACTION_DIGIT = 4;
    private static final int NUMBER_CHAR_EXP_E = 5;
    private static final int NUMBER_CHAR_EXP_SIGN = 6;
    private static final int NUMBER_CHAR_EXP_DIGIT = 7;
    private final Reader in;
    private boolean lenient = false;
    private final char[] buffer = new char[1024];
    private int pos = 0;
    private int limit = 0;
    private int lineNumber = 0;
    private int lineStart = 0;
    int peeked = 0;
    private long peekedLong;
    private int peekedNumberLength;
    private String peekedString;
    private int[] stack = new int[32];
    private int stackSize = 0;
    private String[] pathNames;
    private int[] pathIndices;

    public JsonReader(Reader reader) {
        this.stack[this.stackSize++] = 6;
        this.pathNames = new String[32];
        this.pathIndices = new int[32];
        if (reader == null) {
            throw new NullPointerException("in == null");
        }
        this.in = reader;
    }

    public final void setLenient(boolean bl) {
        this.lenient = bl;
    }

    public final boolean isLenient() {
        return this.lenient;
    }

    public void beginArray() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n != 3) {
            throw new IllegalStateException("Expected BEGIN_ARRAY but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.push(1);
        this.pathIndices[this.stackSize - 1] = 0;
        this.peeked = 0;
    }

    public void endArray() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 4) {
            --this.stackSize;
        } else {
            throw new IllegalStateException("Expected END_ARRAY but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        int[] arrn = this.pathIndices;
        int n2 = this.stackSize - 1;
        arrn[n2] = arrn[n2] + 1;
        this.peeked = 0;
    }

    public void beginObject() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n != 1) {
            throw new IllegalStateException("Expected BEGIN_OBJECT but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.push(3);
        this.peeked = 0;
    }

    public void endObject() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 2) {
            --this.stackSize;
        } else {
            throw new IllegalStateException("Expected END_OBJECT but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.pathNames[this.stackSize] = null;
        int[] arrn = this.pathIndices;
        int n2 = this.stackSize - 1;
        arrn[n2] = arrn[n2] + 1;
        this.peeked = 0;
    }

    public boolean hasNext() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        return n != 2 && n != 4;
    }

    public JsonToken peek() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        switch (n) {
            case 1: {
                return JsonToken.BEGIN_OBJECT;
            }
            case 2: {
                return JsonToken.END_OBJECT;
            }
            case 3: {
                return JsonToken.BEGIN_ARRAY;
            }
            case 4: {
                return JsonToken.END_ARRAY;
            }
            case 12: 
            case 13: 
            case 14: {
                return JsonToken.NAME;
            }
            case 5: 
            case 6: {
                return JsonToken.BOOLEAN;
            }
            case 7: {
                return JsonToken.NULL;
            }
            case 8: 
            case 9: 
            case 10: 
            case 11: {
                return JsonToken.STRING;
            }
            case 15: 
            case 16: {
                return JsonToken.NUMBER;
            }
            case 17: {
                return JsonToken.END_DOCUMENT;
            }
        }
        throw new AssertionError();
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    int doPeek() {
        var1_1 = this.stack[this.stackSize - 1];
        if (var1_1 != 1) ** GOTO lbl5
        this.stack[this.stackSize - 1] = 2;
        ** GOTO lbl76
lbl5: // 1 sources:
        if (var1_1 != 2) ** GOTO lbl17
        var2_2 = this.nextNonWhitespace(true);
        switch (var2_2) {
            case 93: {
                this.peeked = 4;
                return 4;
            }
            case 59: {
                this.checkLenient();
            }
            case 44: {
                ** GOTO lbl76
            }
            default: {
                throw this.syntaxError("Unterminated array");
            }
        }
lbl17: // 1 sources:
        if (var1_1 == 3 || var1_1 == 5) {
            this.stack[this.stackSize - 1] = 4;
            if (var1_1 == 5) {
                var2_3 = this.nextNonWhitespace(true);
                switch (var2_3) {
                    case 125: {
                        this.peeked = 2;
                        return 2;
                    }
                    case 59: {
                        this.checkLenient();
                    }
                    case 44: {
                        ** break;
                    }
                }
                throw this.syntaxError("Unterminated object");
            }
lbl30: // 3 sources:
            var2_3 = this.nextNonWhitespace(true);
            switch (var2_3) {
                case 34: {
                    this.peeked = 13;
                    return 13;
                }
                case 39: {
                    this.checkLenient();
                    this.peeked = 12;
                    return 12;
                }
                case 125: {
                    if (var1_1 == 5) throw this.syntaxError("Expected name");
                    this.peeked = 2;
                    return 2;
                }
            }
            this.checkLenient();
            --this.pos;
            if (this.isLiteral((char)var2_3) == false) throw this.syntaxError("Expected name");
            this.peeked = 14;
            return 14;
        }
        if (var1_1 != 4) ** GOTO lbl61
        this.stack[this.stackSize - 1] = 5;
        var2_2 = this.nextNonWhitespace(true);
        switch (var2_2) {
            case 58: {
                ** GOTO lbl76
            }
            case 61: {
                this.checkLenient();
                if ((this.pos < this.limit || this.fillBuffer(1)) && this.buffer[this.pos] == '>') {
                    ++this.pos;
                }
                ** GOTO lbl76
            }
            default: {
                throw this.syntaxError("Expected ':'");
            }
        }
lbl61: // 1 sources:
        if (var1_1 == 6) {
            if (this.lenient) {
                this.consumeNonExecutePrefix();
            }
            this.stack[this.stackSize - 1] = 7;
        } else if (var1_1 == 7) {
            var2_2 = this.nextNonWhitespace(false);
            if (var2_2 == -1) {
                this.peeked = 17;
                return 17;
            }
            this.checkLenient();
            --this.pos;
        } else if (var1_1 == 8) {
            throw new IllegalStateException("JsonReader is closed");
        }
lbl76: // 8 sources:
        var2_2 = this.nextNonWhitespace(true);
        switch (var2_2) {
            case 93: {
                if (var1_1 == 1) {
                    this.peeked = 4;
                    return 4;
                }
            }
            case 44: 
            case 59: {
                if (var1_1 != 1) {
                    if (var1_1 != 2) throw this.syntaxError("Unexpected value");
                }
                this.checkLenient();
                --this.pos;
                this.peeked = 7;
                return 7;
            }
            case 39: {
                this.checkLenient();
                this.peeked = 8;
                return 8;
            }
            case 34: {
                this.peeked = 9;
                return 9;
            }
            case 91: {
                this.peeked = 3;
                return 3;
            }
            case 123: {
                this.peeked = 1;
                return 1;
            }
        }
        --this.pos;
        var3_4 = this.peekKeyword();
        if (var3_4 != 0) {
            return var3_4;
        }
        var3_4 = this.peekNumber();
        if (var3_4 != 0) {
            return var3_4;
        }
        if (!this.isLiteral(this.buffer[this.pos])) {
            throw this.syntaxError("Expected value");
        }
        this.checkLenient();
        this.peeked = 10;
        return 10;
    }

    private int peekKeyword() {
        String string;
        String string2;
        int n;
        char c = this.buffer[this.pos];
        if (c == 't' || c == 'T') {
            string2 = "true";
            string = "TRUE";
            n = 5;
        } else if (c == 'f' || c == 'F') {
            string2 = "false";
            string = "FALSE";
            n = 6;
        } else if (c == 'n' || c == 'N') {
            string2 = "null";
            string = "NULL";
            n = 7;
        } else {
            return 0;
        }
        int n2 = string2.length();
        for (int i = 1; i < n2; ++i) {
            if (this.pos + i >= this.limit && !this.fillBuffer(i + 1)) {
                return 0;
            }
            c = this.buffer[this.pos + i];
            if (c == string2.charAt(i) || c == string.charAt(i)) continue;
            return 0;
        }
        if ((this.pos + n2 < this.limit || this.fillBuffer(n2 + 1)) && this.isLiteral(this.buffer[this.pos + n2])) {
            return 0;
        }
        this.pos += n2;
        this.peeked = n;
        return this.peeked;
    }

    private int peekNumber() {
        char[] arrc = this.buffer;
        int n = this.pos;
        int n2 = this.limit;
        long l = 0;
        boolean bl = false;
        boolean bl2 = true;
        int n3 = 0;
        int n4 = 0;
        block6 : do {
            if (n + n4 == n2) {
                if (n4 == arrc.length) {
                    return 0;
                }
                if (!this.fillBuffer(n4 + 1)) break;
                n = this.pos;
                n2 = this.limit;
            }
            char c = arrc[n + n4];
            switch (c) {
                case '-': {
                    if (n3 == 0) {
                        bl = true;
                        n3 = 1;
                        break;
                    }
                    if (n3 == 5) {
                        n3 = 6;
                        break;
                    }
                    return 0;
                }
                case '+': {
                    if (n3 == 5) {
                        n3 = 6;
                        break;
                    }
                    return 0;
                }
                case 'E': 
                case 'e': {
                    if (n3 == 2 || n3 == 4) {
                        n3 = 5;
                        break;
                    }
                    return 0;
                }
                case '.': {
                    if (n3 == 2) {
                        n3 = 3;
                        break;
                    }
                    return 0;
                }
                default: {
                    if (c < '0' || c > '9') {
                        if (!this.isLiteral(c)) break block6;
                        return 0;
                    }
                    if (n3 == 1 || n3 == 0) {
                        l = - c - 48;
                        n3 = 2;
                        break;
                    }
                    if (n3 == 2) {
                        if (l == 0) {
                            return 0;
                        }
                        long l2 = l * 10 - (long)(c - 48);
                        bl2 &= l > -922337203685477580L || l == -922337203685477580L && l2 < l;
                        l = l2;
                        break;
                    }
                    if (n3 == 3) {
                        n3 = 4;
                        break;
                    }
                    if (n3 != 5 && n3 != 6) break;
                    n3 = 7;
                }
            }
            ++n4;
        } while (true);
        if (n3 == 2 && bl2 && (l != Long.MIN_VALUE || bl)) {
            this.peekedLong = bl ? l : - l;
            this.pos += n4;
            this.peeked = 15;
            return 15;
        }
        if (n3 == 2 || n3 == 4 || n3 == 7) {
            this.peekedNumberLength = n4;
            this.peeked = 16;
            return 16;
        }
        return 0;
    }

    private boolean isLiteral(char c) {
        switch (c) {
            case '#': 
            case '/': 
            case ';': 
            case '=': 
            case '\\': {
                this.checkLenient();
            }
            case '\t': 
            case '\n': 
            case '\f': 
            case '\r': 
            case ' ': 
            case ',': 
            case ':': 
            case '[': 
            case ']': 
            case '{': 
            case '}': {
                return false;
            }
        }
        return true;
    }

    public String nextName() {
        String string;
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 14) {
            string = this.nextUnquotedValue();
        } else if (n == 12) {
            string = this.nextQuotedValue('\'');
        } else if (n == 13) {
            string = this.nextQuotedValue('\"');
        } else {
            throw new IllegalStateException("Expected a name but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = string;
        return string;
    }

    public String nextString() {
        String string;
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 10) {
            string = this.nextUnquotedValue();
        } else if (n == 8) {
            string = this.nextQuotedValue('\'');
        } else if (n == 9) {
            string = this.nextQuotedValue('\"');
        } else if (n == 11) {
            string = this.peekedString;
            this.peekedString = null;
        } else if (n == 15) {
            string = Long.toString(this.peekedLong);
        } else if (n == 16) {
            string = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else {
            throw new IllegalStateException("Expected a string but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n2 = this.stackSize - 1;
        arrn[n2] = arrn[n2] + 1;
        return string;
    }

    public boolean nextBoolean() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 5) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n2 = this.stackSize - 1;
            arrn[n2] = arrn[n2] + 1;
            return true;
        }
        if (n == 6) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n3 = this.stackSize - 1;
            arrn[n3] = arrn[n3] + 1;
            return false;
        }
        throw new IllegalStateException("Expected a boolean but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
    }

    public void nextNull() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n != 7) {
            throw new IllegalStateException("Expected null but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n2 = this.stackSize - 1;
        arrn[n2] = arrn[n2] + 1;
    }

    public double nextDouble() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n2 = this.stackSize - 1;
            arrn[n2] = arrn[n2] + 1;
            return this.peekedLong;
        }
        if (n == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n == 8 || n == 9) {
            this.peekedString = this.nextQuotedValue(n == 8 ? '\'' : '\"');
        } else if (n == 10) {
            this.peekedString = this.nextUnquotedValue();
        } else if (n != 11) {
            throw new IllegalStateException("Expected a double but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peeked = 11;
        double d = Double.parseDouble(this.peekedString);
        if (!this.lenient && (Double.isNaN(d) || Double.isInfinite(d))) {
            throw new MalformedJsonException("JSON forbids NaN and infinities: " + d + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n3 = this.stackSize - 1;
        arrn[n3] = arrn[n3] + 1;
        return d;
    }

    public long nextLong() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n2 = this.stackSize - 1;
            arrn[n2] = arrn[n2] + 1;
            return this.peekedLong;
        }
        if (n == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n == 8 || n == 9) {
            this.peekedString = this.nextQuotedValue(n == 8 ? '\'' : '\"');
            try {
                long l = Long.parseLong(this.peekedString);
                this.peeked = 0;
                int[] arrn = this.pathIndices;
                int n3 = this.stackSize - 1;
                arrn[n3] = arrn[n3] + 1;
                return l;
            }
            catch (NumberFormatException var2_3) {}
        } else {
            throw new IllegalStateException("Expected a long but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peeked = 11;
        double d = Double.parseDouble(this.peekedString);
        long l = (long)d;
        if ((double)l != d) {
            throw new NumberFormatException("Expected a long but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n4 = this.stackSize - 1;
        arrn[n4] = arrn[n4] + 1;
        return l;
    }

    private String nextQuotedValue(char c) {
        char[] arrc = this.buffer;
        StringBuilder stringBuilder = new StringBuilder();
        do {
            int n = this.pos;
            int n2 = this.limit;
            int n3 = n;
            while (n < n2) {
                char c2;
                if ((c2 = arrc[n++]) == c) {
                    this.pos = n;
                    stringBuilder.append(arrc, n3, n - n3 - 1);
                    return stringBuilder.toString();
                }
                if (c2 == '\\') {
                    this.pos = n;
                    stringBuilder.append(arrc, n3, n - n3 - 1);
                    stringBuilder.append(this.readEscapeCharacter());
                    n = this.pos;
                    n2 = this.limit;
                    n3 = n;
                    continue;
                }
                if (c2 != '\n') continue;
                ++this.lineNumber;
                this.lineStart = n;
            }
            stringBuilder.append(arrc, n3, n - n3);
            this.pos = n;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }

    private String nextUnquotedValue() {
        String string;
        StringBuilder stringBuilder = null;
        int n = 0;
        block4 : do {
            if (this.pos + n < this.limit) {
                switch (this.buffer[this.pos + n]) {
                    case '#': 
                    case '/': 
                    case ';': 
                    case '=': 
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case ',': 
                    case ':': 
                    case '[': 
                    case ']': 
                    case '{': 
                    case '}': {
                        break block4;
                    }
                }
                ++n;
                continue;
            }
            if (n < this.buffer.length) {
                if (!this.fillBuffer(n + 1)) break;
                continue;
            }
            if (stringBuilder == null) {
                stringBuilder = new StringBuilder();
            }
            stringBuilder.append(this.buffer, this.pos, n);
            this.pos += n;
            n = 0;
            if (!this.fillBuffer(1)) break;
        } while (true);
        if (stringBuilder == null) {
            string = new String(this.buffer, this.pos, n);
        } else {
            stringBuilder.append(this.buffer, this.pos, n);
            string = stringBuilder.toString();
        }
        this.pos += n;
        return string;
    }

    private void skipQuotedValue(char c) {
        char[] arrc = this.buffer;
        do {
            int n = this.pos;
            int n2 = this.limit;
            while (n < n2) {
                char c2;
                if ((c2 = arrc[n++]) == c) {
                    this.pos = n;
                    return;
                }
                if (c2 == '\\') {
                    this.pos = n;
                    this.readEscapeCharacter();
                    n = this.pos;
                    n2 = this.limit;
                    continue;
                }
                if (c2 != '\n') continue;
                ++this.lineNumber;
                this.lineStart = n;
            }
            this.pos = n;
        } while (this.fillBuffer(1));
        throw this.syntaxError("Unterminated string");
    }

    private void skipUnquotedValue() {
        do {
            int n = 0;
            while (this.pos + n < this.limit) {
                switch (this.buffer[this.pos + n]) {
                    case '#': 
                    case '/': 
                    case ';': 
                    case '=': 
                    case '\\': {
                        this.checkLenient();
                    }
                    case '\t': 
                    case '\n': 
                    case '\f': 
                    case '\r': 
                    case ' ': 
                    case ',': 
                    case ':': 
                    case '[': 
                    case ']': 
                    case '{': 
                    case '}': {
                        this.pos += n;
                        return;
                    }
                }
                ++n;
            }
            this.pos += n;
        } while (this.fillBuffer(1));
    }

    public int nextInt() {
        int n = this.peeked;
        if (n == 0) {
            n = this.doPeek();
        }
        if (n == 15) {
            int n2 = (int)this.peekedLong;
            if (this.peekedLong != (long)n2) {
                throw new NumberFormatException("Expected an int but was " + this.peekedLong + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
            }
            this.peeked = 0;
            int[] arrn = this.pathIndices;
            int n3 = this.stackSize - 1;
            arrn[n3] = arrn[n3] + 1;
            return n2;
        }
        if (n == 16) {
            this.peekedString = new String(this.buffer, this.pos, this.peekedNumberLength);
            this.pos += this.peekedNumberLength;
        } else if (n == 8 || n == 9) {
            this.peekedString = this.nextQuotedValue(n == 8 ? '\'' : '\"');
            try {
                int n4 = Integer.parseInt(this.peekedString);
                this.peeked = 0;
                int[] arrn = this.pathIndices;
                int n5 = this.stackSize - 1;
                arrn[n5] = arrn[n5] + 1;
                return n4;
            }
            catch (NumberFormatException var3_5) {}
        } else {
            throw new IllegalStateException("Expected an int but was " + (Object)((Object)this.peek()) + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peeked = 11;
        double d = Double.parseDouble(this.peekedString);
        int n6 = (int)d;
        if ((double)n6 != d) {
            throw new NumberFormatException("Expected an int but was " + this.peekedString + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
        }
        this.peekedString = null;
        this.peeked = 0;
        int[] arrn = this.pathIndices;
        int n7 = this.stackSize - 1;
        arrn[n7] = arrn[n7] + 1;
        return n6;
    }

    @Override
    public void close() {
        this.peeked = 0;
        this.stack[0] = 8;
        this.stackSize = 1;
        this.in.close();
    }

    public void skipValue() {
        int n = 0;
        do {
            int n2;
            if ((n2 = this.peeked) == 0) {
                n2 = this.doPeek();
            }
            if (n2 == 3) {
                this.push(1);
                ++n;
            } else if (n2 == 1) {
                this.push(3);
                ++n;
            } else if (n2 == 4) {
                --this.stackSize;
                --n;
            } else if (n2 == 2) {
                --this.stackSize;
                --n;
            } else if (n2 == 14 || n2 == 10) {
                this.skipUnquotedValue();
            } else if (n2 == 8 || n2 == 12) {
                this.skipQuotedValue('\'');
            } else if (n2 == 9 || n2 == 13) {
                this.skipQuotedValue('\"');
            } else if (n2 == 16) {
                this.pos += this.peekedNumberLength;
            }
            this.peeked = 0;
        } while (n != 0);
        int[] arrn = this.pathIndices;
        int n3 = this.stackSize - 1;
        arrn[n3] = arrn[n3] + 1;
        this.pathNames[this.stackSize - 1] = "null";
    }

    private void push(int n) {
        if (this.stackSize == this.stack.length) {
            int[] arrn = new int[this.stackSize * 2];
            int[] arrn2 = new int[this.stackSize * 2];
            String[] arrstring = new String[this.stackSize * 2];
            System.arraycopy(this.stack, 0, arrn, 0, this.stackSize);
            System.arraycopy(this.pathIndices, 0, arrn2, 0, this.stackSize);
            System.arraycopy(this.pathNames, 0, arrstring, 0, this.stackSize);
            this.stack = arrn;
            this.pathIndices = arrn2;
            this.pathNames = arrstring;
        }
        this.stack[this.stackSize++] = n;
    }

    private boolean fillBuffer(int n) {
        int n2;
        char[] arrc = this.buffer;
        this.lineStart -= this.pos;
        if (this.limit != this.pos) {
            this.limit -= this.pos;
            System.arraycopy(arrc, this.pos, arrc, 0, this.limit);
        } else {
            this.limit = 0;
        }
        this.pos = 0;
        while ((n2 = this.in.read(arrc, this.limit, arrc.length - this.limit)) != -1) {
            this.limit += n2;
            if (this.lineNumber == 0 && this.lineStart == 0 && this.limit > 0 && arrc[0] == '\ufeff') {
                ++this.pos;
                ++this.lineStart;
                ++n;
            }
            if (this.limit < n) continue;
            return true;
        }
        return false;
    }

    int getLineNumber() {
        return this.lineNumber + 1;
    }

    int getColumnNumber() {
        return this.pos - this.lineStart + 1;
    }

    private int nextNonWhitespace(boolean bl) {
        block12 : {
            char c;
            char[] arrc = this.buffer;
            int n = this.pos;
            int n2 = this.limit;
            block4 : do {
                if (n == n2) {
                    this.pos = n;
                    if (!this.fillBuffer(1)) break block12;
                    n = this.pos;
                    n2 = this.limit;
                }
                if ((c = arrc[n++]) == '\n') {
                    ++this.lineNumber;
                    this.lineStart = n;
                    continue;
                }
                if (c == ' ' || c == '\r' || c == '\t') continue;
                if (c == '/') {
                    char c2;
                    this.pos = n;
                    if (n == n2) {
                        --this.pos;
                        c2 = (char)this.fillBuffer(2) ? 1 : 0;
                        ++this.pos;
                        if (c2 == '\u0000') {
                            return c;
                        }
                    }
                    this.checkLenient();
                    c2 = arrc[this.pos];
                    switch (c2) {
                        case '*': {
                            ++this.pos;
                            if (!this.skipTo("*/")) {
                                throw this.syntaxError("Unterminated comment");
                            }
                            n = this.pos + 2;
                            n2 = this.limit;
                            continue block4;
                        }
                        case '/': {
                            ++this.pos;
                            this.skipToEndOfLine();
                            n = this.pos;
                            n2 = this.limit;
                            continue block4;
                        }
                    }
                    return c;
                }
                if (c != '#') break;
                this.pos = n;
                this.checkLenient();
                this.skipToEndOfLine();
                n = this.pos;
                n2 = this.limit;
            } while (true);
            this.pos = n;
            return c;
        }
        if (bl) {
            throw new EOFException("End of input at line " + this.getLineNumber() + " column " + this.getColumnNumber());
        }
        return -1;
    }

    private void checkLenient() {
        if (!this.lenient) {
            throw this.syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        }
    }

    private void skipToEndOfLine() {
        while (this.pos < this.limit || this.fillBuffer(1)) {
            char c;
            if ((c = this.buffer[this.pos++]) == '\n') {
                ++this.lineNumber;
                this.lineStart = this.pos;
                break;
            }
            if (c != '\r') continue;
            break;
        }
    }

    private boolean skipTo(String string) {
        while (this.pos + string.length() <= this.limit || this.fillBuffer(string.length())) {
            block5 : {
                if (this.buffer[this.pos] == '\n') {
                    ++this.lineNumber;
                    this.lineStart = this.pos + 1;
                } else {
                    for (int i = 0; i < string.length(); ++i) {
                        if (this.buffer[this.pos + i] == string.charAt(i)) {
                            continue;
                        }
                        break block5;
                    }
                    return true;
                }
            }
            ++this.pos;
        }
        return false;
    }

    public String toString() {
        return this.getClass().getSimpleName() + " at line " + this.getLineNumber() + " column " + this.getColumnNumber();
    }

    public String getPath() {
        StringBuilder stringBuilder = new StringBuilder().append('$');
        int n = this.stackSize;
        block4 : for (int i = 0; i < n; ++i) {
            switch (this.stack[i]) {
                case 1: 
                case 2: {
                    stringBuilder.append('[').append(this.pathIndices[i]).append(']');
                    continue block4;
                }
                case 3: 
                case 4: 
                case 5: {
                    stringBuilder.append('.');
                    if (this.pathNames[i] == null) continue block4;
                    stringBuilder.append(this.pathNames[i]);
                    break;
                }
            }
        }
        return stringBuilder.toString();
    }

    private char readEscapeCharacter() {
        if (this.pos == this.limit && !this.fillBuffer(1)) {
            throw this.syntaxError("Unterminated escape sequence");
        }
        char c = this.buffer[this.pos++];
        switch (c) {
            case 'u': {
                int n;
                if (this.pos + 4 > this.limit && !this.fillBuffer(4)) {
                    throw this.syntaxError("Unterminated escape sequence");
                }
                char c2 = '\u0000';
                int n2 = n + 4;
                for (n = this.pos; n < n2; ++n) {
                    char c3 = this.buffer[n];
                    c2 = (char)(c2 << 4);
                    if (c3 >= '0' && c3 <= '9') {
                        c2 = (char)(c2 + (c3 - 48));
                        continue;
                    }
                    if (c3 >= 'a' && c3 <= 'f') {
                        c2 = (char)(c2 + (c3 - 97 + 10));
                        continue;
                    }
                    if (c3 >= 'A' && c3 <= 'F') {
                        c2 = (char)(c2 + (c3 - 65 + 10));
                        continue;
                    }
                    throw new NumberFormatException("\\u" + new String(this.buffer, this.pos, 4));
                }
                this.pos += 4;
                return c2;
            }
            case 't': {
                return '\t';
            }
            case 'b': {
                return '\b';
            }
            case 'n': {
                return '\n';
            }
            case 'r': {
                return '\r';
            }
            case 'f': {
                return '\f';
            }
            case '\n': {
                ++this.lineNumber;
                this.lineStart = this.pos;
            }
        }
        return c;
    }

    private IOException syntaxError(String string) {
        throw new MalformedJsonException(string + " at line " + this.getLineNumber() + " column " + this.getColumnNumber() + " path " + this.getPath());
    }

    private void consumeNonExecutePrefix() {
        this.nextNonWhitespace(true);
        --this.pos;
        if (this.pos + NON_EXECUTE_PREFIX.length > this.limit && !this.fillBuffer(NON_EXECUTE_PREFIX.length)) {
            return;
        }
        for (int i = 0; i < NON_EXECUTE_PREFIX.length; ++i) {
            if (this.buffer[this.pos + i] == NON_EXECUTE_PREFIX[i]) continue;
            return;
        }
        this.pos += NON_EXECUTE_PREFIX.length;
    }

    static {
        JsonReaderInternalAccess.INSTANCE = new JsonReaderInternalAccess(){

            @Override
            public void promoteNameToValue(JsonReader jsonReader) {
                if (jsonReader instanceof JsonTreeReader) {
                    ((JsonTreeReader)jsonReader).promoteNameToValue();
                    return;
                }
                int n = jsonReader.peeked;
                if (n == 0) {
                    n = jsonReader.doPeek();
                }
                if (n == 13) {
                    jsonReader.peeked = 9;
                } else if (n == 12) {
                    jsonReader.peeked = 8;
                } else if (n == 14) {
                    jsonReader.peeked = 10;
                } else {
                    throw new IllegalStateException("Expected a name but was " + (Object)((Object)jsonReader.peek()) + " " + " at line " + jsonReader.getLineNumber() + " column " + jsonReader.getColumnNumber() + " path " + jsonReader.getPath());
                }
            }
        };
    }

}

