/*
 * Copyright 2012-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package irt.stock.web.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("storage")
public class StorageController {
//	private final static Logger logger = LogManager.getLogger();

	@Value("${irt.files.default.storage.location}") private String rootLocation;

	@GetMapping(value = "{group}/{groupId}/{subgroupId}/{filename}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public FileSystemResource searchPage(@PathVariable String group, @PathVariable String groupId, @PathVariable String subgroupId, @PathVariable String filename) {

		Path path = Paths.get(rootLocation, group, groupId, subgroupId, filename);
		return new FileSystemResource(path);
	}
}
