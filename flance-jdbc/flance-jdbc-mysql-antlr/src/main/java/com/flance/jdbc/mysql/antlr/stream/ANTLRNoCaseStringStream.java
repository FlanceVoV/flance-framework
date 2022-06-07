/*
 *  Copyright 1999-2019 Seata.io Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.flance.jdbc.mysql.antlr.stream;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.IntStream;

/**
 * ANTLRNoCaseStringStream
 *
 * @author zhihou
 */
public class ANTLRNoCaseStringStream extends ANTLRInputStream {

    public ANTLRNoCaseStringStream(String input) {
        super(input);
    }

    @Override
    public int LA(int i) {
        int la = super.LA(i);
        if (la == 0 || la == IntStream.EOF) {
            return la;
        } else {
            return Character.toUpperCase(la);
        }
    }
}
