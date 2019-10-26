/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.dubbo.monitor.simple.pages;

import org.apache.dubbo.monitor.simple.common.Menu;
import org.apache.dubbo.monitor.simple.common.Page;
import org.apache.dubbo.monitor.simple.servlet.PageHandler;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.utils.NetUtils;
import org.apache.dubbo.registry.Registry;
import org.apache.dubbo.registry.support.AbstractRegistry;
import org.apache.dubbo.registry.support.AbstractRegistryFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * RegistriesPageHandler
 */
@Menu(name = "Registries", desc = "Show connected registries.", order = 10000)
public class RegistriesPageHandler implements PageHandler
{

    @Override
    public Page handle(URL url) {
        List<List<String>> rows = new ArrayList<List<String>>();
        Collection<Registry> registries = AbstractRegistryFactory.getRegistries();
        int registeredCount = 0;
        int subscribedCount = 0;
        if (registries != null && registries.size() > 0) {
            for (Registry registry : registries) {
                String server = registry.getUrl().getAddress();
                List<String> row = new ArrayList<String>();
                row.add(NetUtils.getHostName(server) + "/" + server);
                if (registry.isAvailable()) {
                    row.add("<font color=\"green\">Connected</font>");
                } else {
                    row.add("<font color=\"red\">Disconnected</font>");
                }
                int registeredSize = 0;
                int subscribedSize = 0;
                if (registry instanceof AbstractRegistry) {
                    registeredSize = ((AbstractRegistry) registry).getRegistered().size();
                    registeredCount += registeredSize;
                    subscribedSize = ((AbstractRegistry) registry).getSubscribed().size();
                    subscribedCount += subscribedSize;
                }
                row.add("<a href=\"registered.html?registry=" + server + "\">Registered(" + registeredSize + ")</a>");
                row.add("<a href=\"subscribed.html?registry=" + server + "\">Subscribed(" + subscribedSize + ")</a>");
                rows.add(row);
            }
        }
        return new Page("Registries", "Registries (" + rows.size() + ")",
                new String[]{"Registry Address:", "Status", "Registered(" + registeredCount + ")", "Subscribed(" + subscribedCount + ")"}, rows);
    }

}