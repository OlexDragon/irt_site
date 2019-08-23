package irt.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import irt.entities.LinkEntity;
import irt.entities.repository.LinkRepository;

@RestController
public class RestFileWorker {
	private final Logger logger = LogManager.getLogger();

	@Autowired LinkRepository repository;

	@Value("${irt.lib.folder}")private String libPath;

	@RequestMapping("/download/{link_id}")
	public ResponseEntity<byte[]> download( @PathVariable("link_id") Long linkId){

		try {
			final LinkEntity link = repository.getOne(linkId);

			HttpHeaders headers = new HttpHeaders();
			File file = Paths.get(libPath, link.getLink()).toFile();

			if (file.exists()) {

				headers.setContentLength(file.length());
				headers.set("Content-Disposition", "filename=\"" + file.getName() + "\"");
				headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);

				try (FileInputStream is = new FileInputStream(file);
						ByteArrayOutputStream os = new ByteArrayOutputStream();) {

					StreamUtils.copy(is, os);
					byte[] byteArray = os.toByteArray();

					return new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);

				}

			}
		} catch (Exception e) {
			logger.catching(e);
		}

		return new ResponseEntity<byte[]>(HttpStatus.NOT_FOUND);
	}

	@RequestMapping(value={"/upload/{component_group}"}, headers = "content-type=multipart/*", method = RequestMethod.POST)
	public ResponseEntity<String> upload(@RequestParam("fileSelect") MultipartFile file, @PathVariable(value="component_group") String componentGroup){
		return upload(file, componentGroup, null);
	}

	@RequestMapping(value={"/upload/{component_group}/{link_id}"}, headers = "content-type=multipart/*", method = RequestMethod.POST)
	public ResponseEntity<String> upload(@RequestParam("fileSelect") MultipartFile file, @PathVariable(value="component_group") String componentGroup, @PathVariable(value="link_id") Long linkId){

		final String filename = file.getOriginalFilename();
		logger.debug("upload file {}", filename);

		boolean fileSaved = false;
		if (!file.isEmpty()) {

			Path root = createFolder(componentGroup);
			final Path path = Paths.get(root.toString(), filename);
			File f = path.toFile();

			Optional<Path> backup = Optional.empty();
			try {
				backup = backup(linkId, f);

				fileSaved = Files.copy(file.getInputStream(), path) > 0;
				logger.debug("The file '{}' has been uploaded", path);

				LinkEntity link;
				if(linkId!=null){
					link = repository.findById(linkId).get();
				}else{
					link = new LinkEntity();
					link.setId(repository.findNewId());
				}

				link.setLink("/" + Paths.get(libPath).relativize(path).toString());
				repository.save(link);
				logger.debug("{} has been saved to the database", link);

				return ResponseEntity.ok().body(link.getId().toString());

			} catch (Exception e) {

				if(fileSaved){
					f.delete();
					logger.debug("Uploaded file '{}' has been deleted");
				}

				backup.ifPresent(p->{
					try {

						Files.move(p, f.toPath());
						logger.debug("The file {} has been moved back fom the backup.", f);

					} catch (Exception e1) {
						logger.catching(e1);
					}
				});
				logger.catching(e);
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload " + filename + " => " + e.getMessage());
			}
		}else{
			logger.debug("File {} is empty", filename);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Failed to upload " + filename + " because it was empty");
		}
	}

	private Optional<Path> backup(Long linkId, File f) throws IOException {

		if(f.exists()){
			final Path pathFrom = f.toPath();

			//file name
			String filename = "";
			if(linkId!=null)
				filename = linkId + "_";
			filename += f.getName() + "." + System.currentTimeMillis();
			final Path backupFolder = Paths.get(f.getParent(), "backup");
			final File bFile = backupFolder.toFile();

			if(!bFile.exists())
				bFile.mkdirs();
			
			final Path pathTo = Paths.get(f.getParent(), "backup", filename);

			final Path move = Files.move(pathFrom, pathTo);
			logger.debug("The file '{}' has been moved to '{}'", pathFrom, pathTo);
			return Optional.of(move);
		}

		return Optional.empty();
	}

	private Path createFolder(String componentGroup) {
		final Path path = Paths.get(libPath, "irt", "lib", componentGroup);
		final File file = path.toFile();
		if(!file.exists()){
			file.mkdirs();
			logger.debug("New folder '{}' has been created", file);
		}
		return path;
	}
}
