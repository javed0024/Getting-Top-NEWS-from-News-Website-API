package Stories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class TopStories {
    @GetMapping("/getTimeStories")
    public List<StoriesResponseDTO> getTopStories() throws JsonProcessingException {
            String url = "https://api.spokenlayer.net/web-player/playlist?channel=time-com&_v=alpha&n=9";
            RestTemplate restTemplate = new RestTemplate();
            List<String> titles = new ArrayList<>();
            List<String> urls = new ArrayList<>();
            List<StoriesResponseDTO> storiesResponseDTOs = new ArrayList<>();
            String timesApiJson = restTemplate.postForObject(url, restTemplate.headForHeaders(url), String.class);
            JsonNode node = new ObjectMapper().readTree(timesApiJson);
            if (node.isObject()) {
                    titles = node.findValuesAsText("title", titles);
                    urls = node.findValuesAsText("url", urls);
            }
            for(int i=0;i<Math.min(titles.size(), urls.size());++i)
                storiesResponseDTOs.add(new StoriesResponseDTO(titles.get(i),urls.get(i)));

            return storiesResponseDTOs.stream().limit(5).collect(Collectors.toList());
    }
}