/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.facebook.presto.orc.stream;

import com.google.common.base.Objects;
import com.google.common.io.ByteSource;

import java.io.IOException;

public class ByteStreamSource
        implements StreamSource<ByteStream>
{
    private final ByteSource byteSource;
    private final int byteValueSkipSize;

    public ByteStreamSource(ByteSource byteSource, int byteValueSkipSize)
    {
        this.byteSource = byteSource;
        this.byteValueSkipSize = byteValueSkipSize;
    }

    @Override
    public Class<ByteStream> getStreamType()
    {
        return ByteStream.class;
    }

    @Override
    public ByteStream openStream()
            throws IOException
    {
        ByteStream byteReader = new ByteStream(byteSource.openStream());
        if (byteValueSkipSize > 0) {
            byteReader.skip(byteValueSkipSize);
        }
        return byteReader;
    }

    @Override
    public String toString()
    {
        return Objects.toStringHelper(this)
                .add("byteSource", byteSource)
                .add("byteValueSkipSize", byteValueSkipSize)
                .toString();
    }
}
