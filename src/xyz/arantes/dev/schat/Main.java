package xyz.arantes.dev.schat;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements CommandExecutor {

    private static Chat chat;

    @Override
    public void onEnable() {
        if (!setupChat()){
            getLogger().info("Não foi possível se conectar com o vault. Desativando.");
            getServer().getPluginManager().disablePlugin(this);
        }
        getCommand("s").setExecutor(this);
    }

    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)){
            sender.sendMessage("§cApenas jogadores in-game.");
            return true;
        }
        Player player = (Player) sender;

        if (!player.hasPermission("alpha.schat")){
            player.sendMessage("§cSem permissão!");
        }else{
            if (args.length < 1){
                player.sendMessage("§cUso correto: /s <mensagem>");
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (String s : args){
                message.append(s);
                message.append(" ");
            }

            for (Player online : Bukkit.getOnlinePlayers()){
                if (online.hasPermission("alpha.schat")){
                    online.sendMessage("§c[Staff] §f" +
                            ChatColor.translateAlternateColorCodes('&', chat.getPlayerPrefix(player)) +
                            "§f " + player.getDisplayName() + " §7>> §f" +
                            ChatColor.translateAlternateColorCodes('&', message.toString()));
                }
            }
        }
        return true;
    }
}
