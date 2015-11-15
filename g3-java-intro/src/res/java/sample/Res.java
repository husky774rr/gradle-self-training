/*
 * Copyright 2015 Shinya Mochida
 *
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Res {

    private final Properties prop;

    public Res() throws IOException {
        ClassLoader cl = getClass().getClassLoader();
        prop = new Properties();
        try(InputStream is = cl.getResourceAsStream("app.properties")) {
            InputStreamReader r = new InputStreamReader(is, StandardCharsets.UTF_8);
            prop.load(r);
        }
    }

    public String getMessage(String key) {
        return prop.getProperty(key, "<no-entry>");
    }
}
