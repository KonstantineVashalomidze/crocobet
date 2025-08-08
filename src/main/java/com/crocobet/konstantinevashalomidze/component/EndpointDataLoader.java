package com.crocobet.konstantinevashalomidze.component;

import com.crocobet.konstantinevashalomidze.json.request.CreateMonitorRequest;
import com.crocobet.konstantinevashalomidze.model.HttpMethod;
import com.crocobet.konstantinevashalomidze.model.MonitoredEndpoint;
import com.crocobet.konstantinevashalomidze.repository.EndpointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class EndpointDataLoader implements CommandLineRunner {

    private final EndpointRepository endpointRepository;

    @Autowired
    public EndpointDataLoader(EndpointRepository endpointRepository) {
        this.endpointRepository = endpointRepository;
    }

    @Override
    public void run(String... args) {
        loadDefaultEndpoints();
    }

    private void loadDefaultEndpoints() {
        String[] urls = {
                "https://crocobet.com",
                "https://crocobet.com/sport/pre-match?lang=ka",
                "https://crocobet.com/sport/live?lang=ka&defaultSportId=-3",
                "https://crocobet.com/slots?primaryFilter=main&tertiaryFilter=all",
                "https://crocobet.com/casino",
                "https://crocobet.com/mini-games",
                "https://crocobet.com/games",
                "https://crocobet.com/poker",
                "https://crocobet.com/free-sports",
                "https://crocobet.com/promos/promos-main",
                "https://crocobet.com/sport/setanta?lang=ka",
                "https://api.crocobet.com/livebetting-api/rest/livebetting/v1/api/running/games/major",
                "https://cms.crocobet.com/twitch/live-buttons?platform=DESKTOP",
                "https://cms.crocobet.com/integrations/v2/casino/providers/games/stats",
                "https://www.redditstatic.com/ads/conversions-config/v1/pixel/config/a2_hcjewxxct1t7_telemetry",
                "https://cms.crocobet.com/ui/strings/i18n/ka?category=global",
                "https://cms.crocobet.com/customers/profile/avatars",
                "https://cms.crocobet.com/integrations/v2/slot/providers/pate-play/jackpot",
                "https://cms.crocobet.com/banners?platform=desktop&type=slots&lang=ka",
                "https://cms.crocobet.com/integrations/v2/slot/categories?include=games",
                "https://pixel-config.reddit.com/pixels/a2_hcjewxxct1t7/config",
                "https://cms.crocobet.com/ui/toggles/6790aa35d0ea57d02e9994e3"
        };

        for (String url : urls) {
            String endpointName = generateEndpointName(url);

            MonitoredEndpoint endpoint = new MonitoredEndpoint(
                    new CreateMonitorRequest(
                            endpointName,
                            url,
                            HttpMethod.GET,
                            new HashMap<>(),
                            null,
                            true,
                            "200",
                            null
                    )
            );

            endpointRepository.save(endpoint);
        }

        System.out.println("Loaded " + urls.length + " default endpoints into repository");
    }

    private String generateEndpointName(String url) {
        String name = url.replace("https://", "").replace("http://", "");

        if (name.startsWith("crocobet.com")) {
            if (name.equals("crocobet.com")) {
                return "Main Site";
            } else if (name.contains("sport/pre-match")) {
                return "Pre-match Sports";
            } else if (name.contains("sport/live")) {
                return "Live Sports";
            } else if (name.contains("sport/setanta")) {
                return "Setanta Sports";
            } else if (name.contains("slots")) {
                return "Slots Page";
            } else if (name.contains("casino")) {
                return "Casino Page";
            } else if (name.contains("mini-games")) {
                return "Mini Games";
            } else if (name.contains("games")) {
                return "Games Page";
            } else if (name.contains("poker")) {
                return "Poker Page";
            } else if (name.contains("free-sports")) {
                return "Free Sports";
            } else if (name.contains("promos")) {
                return "Promotions";
            }
        } else if (name.startsWith("api.crocobet.com")) {
            if (name.contains("livebetting")) {
                return "Live Betting API";
            }
        } else if (name.startsWith("cms.crocobet.com")) {
            if (name.contains("twitch")) {
                return "Twitch Integration";
            } else if (name.contains("casino/providers/games/stats")) {
                return "Casino Games Stats";
            } else if (name.contains("i18n")) {
                return "Internationalization Strings";
            } else if (name.contains("avatars")) {
                return "Profile Avatars";
            } else if (name.contains("jackpot")) {
                return "Slot Jackpot";
            } else if (name.contains("banners")) {
                return "Slot Banners";
            } else if (name.contains("slot/categories")) {
                return "Slot Categories";
            } else if (name.contains("toggles")) {
                return "Feature Toggles";
            }
        } else if (name.contains("redditstatic.com")) {
            return "Reddit Ads Config";
        } else if (name.contains("pixel-config.reddit.com")) {
            return "Reddit Pixel Config";
        }

        String[] parts = name.split("/");
        return parts[0].replace("www.", "").replace(".com", "").toUpperCase() + " Endpoint";
    }
}