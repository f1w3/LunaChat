/*
 * @author     ucchy
 * @license    LGPLv3
 * @copyright  Copyright ucchy 2020
 */
package com.github.ucchyocean.lc3.tool;

import com.github.ucchyocean.lc3.util.YamlConfig;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * messages_ja.yml を読んで、Messagesクラス用のメソッドを生成するツール
 *
 * @author ucchy
 */
public class MessageParser {

    private static final String INPUT_FILE_PATH = "src/main/resources/messages_ja.yml";
    private static final String OUTPUT_FILE_PATH = "src/main/java/com/github/ucchyocean/lc3/Messages.java";

    private static final String START_MARKER = "    // === Auto-generated methods area start. ===";
    private static final String END_MARKER = "    // === Auto-generated methods area end. ===";

    private static final List<String> CLICKABLE_MESSAGES;

    static {
        CLICKABLE_MESSAGES = new ArrayList<>();
        Collections.addAll(CLICKABLE_MESSAGES, new String[]{
                "joinMessage", "quitMessage", "banMessage", "kickMessage",
                "muteMessage", "banNGWordMessage", "kickNGWordMessage", "muteNGWordMessage", "banWithExpireMessage",
                "muteWithExpireMessage", "pardonMessage", "unmuteMessage", "expiredBanMessage", "expiredMuteMessage",
                "addModeratorMessage", "removeModeratorMessage", "noRecipientMessage", "listFormat",});
    }

    public static void main(String[] args) {

        // 自動生成メソッドを作成する
        List<String> methods = makeAutoGeneratedMethods();

        // 出力ファイルを全行読み取る
        File file = new File(OUTPUT_FILE_PATH);
        List<String> contents = readAllLines(file);

        // START_MARKERが出てくるまで読み進める
        List<String> result = new ArrayList<>();
        int index = 0;
        while (!contents.get(index).equals(START_MARKER)) {
            result.add(contents.get(index));
            index++;
            if (contents.size() <= index) {
                System.err.println("ERROR! : START_MARKER not found.");
                return;
            }
        }
        result.add(contents.get(index));
        index++;

        // 自動生成メソッドを挿入する
        result.addAll(methods);

        // END_MARKERが出てくるまで読み捨てる
        while (!contents.get(index).equals(END_MARKER)) {
            index++;
            if (contents.size() <= index) {
                System.err.println("ERROR! : END_MARKER not found.");
                return;
            }
        }

        // 残りをそのまま読み込む
        while (contents.size() > index) {
            result.add(contents.get(index));
            index++;
        }

        // ファイルへ出力
        writeAllLines(file, result);
    }

    private static List<String> makeAutoGeneratedMethods() {

        List<String> result = new ArrayList<>();

        YamlConfig yaml = YamlConfig.load(new File(INPUT_FILE_PATH));
        for (String key : yaml.getKeys(false)) {
            String value = yaml.getString(key);

            ArrayList<String> keywords = new ArrayList<>();

            Pattern pattern = Pattern.compile("%([^%]*)%");
            Matcher matcher = pattern.matcher(value);

            while (matcher.find()) {
                keywords.add(matcher.group(1));
            }

            StringBuilder arguments = new StringBuilder();
            for (String keyword : keywords) {
                if (!arguments.isEmpty()) arguments.append(", ");
                arguments.append("Object ").append(keyword);
            }

            // 出力
            if (!CLICKABLE_MESSAGES.contains(key)) {
                result.add("");
                result.add("    /**");
                result.add("     * " + value);
                result.add("     */");
                result.add(String.format(
                        "    public static String %s(%s) {", key, arguments.toString()));
                result.add(String.format(
                        "        String msg = resources.getString(\"%s\");", key));
                result.add(String.format(
                        "        if ( msg == null ) return \"\";", key));
                result.add("        KeywordReplacer kr = new KeywordReplacer(msg);");

                for (String keyword : keywords) {
                    result.add(String.format(
                            "        kr.replace(\"%%%s%%\", %s.toString());", keyword, keyword));
                }
                if (key.startsWith("errmsg")) {
                    result.add("        return Utility.replaceColorCode(resources.getString(\"errorPrefix\", \"\") + kr.toString());");
                } else if (key.startsWith("cmdmsg")) {
                    result.add("        return Utility.replaceColorCode(resources.getString(\"infoPrefix\", \"\") + kr.toString());");
                } else {
                    result.add("        return Utility.replaceColorCode(kr.toString());");
                }
                result.add("    }");
            } else {
                result.add("");
                result.add("    /**");
                result.add("     * " + value);
                result.add("     */");
                result.add(String.format(
                        "    public static BaseComponent[] %s(%s) {", key, arguments.toString()));
                result.add(String.format(
                        "        String msg = resources.getString(\"%s\");", key));
                result.add(String.format(
                        "        if ( msg == null ) return new BaseComponent[0];", key));
                result.add("        ClickableFormat cf = ClickableFormat.makeChannelClickableMessage(msg, channel.toString());");

                for (String keyword : keywords) {
                    if (keyword.equals("%channel%")) continue;
                    result.add(String.format(
                            "        cf.replace(\"%%%s%%\", %s.toString());", keyword, keyword));
                }
                result.add("        return cf.makeTextComponent();");
                result.add("    }");

            }
        }

        return result;
    }

    private static List<String> readAllLines(File file) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    private static void writeAllLines(File file, List<String> lines) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
