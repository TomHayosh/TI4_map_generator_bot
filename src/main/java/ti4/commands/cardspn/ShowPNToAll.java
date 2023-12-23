package ti4.commands.cardspn;

import java.util.Map;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import ti4.generator.Mapper;
import ti4.helpers.Constants;
import ti4.helpers.Helper;
import ti4.map.Game;
import ti4.map.Player;

public class ShowPNToAll extends PNCardsSubcommandData {
    public ShowPNToAll() {
        super(Constants.SHOW_PN_TO_ALL, "Show Promissory Note to table");
        addOptions(new OptionData(OptionType.INTEGER, Constants.PROMISSORY_NOTE_ID, "PN ID that is sent between ()").setRequired(true));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Game activeGame = getActiveGame();
        Player player = activeGame.getPlayer(getUser().getId());
        player = Helper.getGamePlayer(activeGame, player, event, null);
        if (player == null) {
            sendMessage("Player could not be found");
            return;
        }
        OptionMapping option = event.getOption(Constants.PROMISSORY_NOTE_ID);
        if (option == null) {
            sendMessage("Please select what Promissory Note to show to All");
            return;
        }

        int pnIndex = option.getAsInt();
        String pnID = null;
        for (Map.Entry<String, Integer> pn : player.getPromissoryNotes().entrySet()) {
            if (pn.getValue().equals(pnIndex)) {
                pnID = pn.getKey();
                break;
            }
        }

        if (pnID == null) {
            sendMessage("No such Promissory Note ID found, please retry");
            return;
        }

        StringBuilder sb = new StringBuilder("Game: ").append(activeGame.getName())
            .append("\nPlayer: ").append(player.getUserName())
            .append("\nShowed Promissory Note:\n").append(Mapper.getPromissoryNoteLongText(pnID))
            .append("\n");
        player.setPromissoryNote(pnID);
        
        sendMessage(sb.toString());
    }
}
