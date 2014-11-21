/*
 * Copyright 2013  Séven Le Mesle
 *
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
 * 
 */
package fr.xebia.extras.selma.beans;

import java.util.List;

/**
 * Created by slemesle on 21/11/14.
 */
public class SimpleContactsDto {

    private List<SimplePersonDto> contacts;

    public List<SimplePersonDto> getContacts() {
        return contacts;
    }

    public void setContacts(List<SimplePersonDto> contacts) {
        this.contacts = contacts;
    }
}
