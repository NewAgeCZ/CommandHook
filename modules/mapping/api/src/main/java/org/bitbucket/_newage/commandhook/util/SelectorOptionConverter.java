package org.bitbucket._newage.commandhook.util;

import org.bukkit.block.CommandBlock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class converting old argument syntax (r => distance; l, lm => level)
 */
public class SelectorOptionConverter {

    private static final Logger logger = LoggerFactory.getLogger(SelectorOptionConverter.class);

    private static final Pattern radius = Pattern.compile("r=[0-9]*");
    private static final Pattern level = Pattern.compile("l=[0-9]*");
    private static final Pattern levelMore = Pattern.compile("lm=[0-9]*");

    private SelectorOptionConverter() {

    }

    /**
     * Parses old syntax (1.13) into new one.
     * Each conversion updates the block automatically.
     *
     * Note: Rewrites old formulas one by one.
     * That being said, if the Command Block contains multiple (up to n) old syntax's,
     * you need to loop the block n+1 times to finally execute.
     *
     * @param commandBlock Command Block
     * @param exceptionMessage Exception thrown by a Command Block
     * @return true if any conversion succeeded
     */
    public static boolean isUnknownOptionConverted(CommandBlock commandBlock, String exceptionMessage) {
        return isRadiusConvertedToDistance(commandBlock, exceptionMessage)
                || isLConvertedToLevel(commandBlock, exceptionMessage)
                || isLmConvertedToLevel(commandBlock, exceptionMessage);
    }

    private static boolean isRadiusConvertedToDistance(CommandBlock commandBlock, String exceptionMessage) {
        boolean success = false;
        if (exceptionMessage.contains("Unknown option 'r'")) {
            Matcher radiusMatcher = radius.matcher(commandBlock.getCommand());

            if (radiusMatcher.find()) {
                logger.info("Attempting to fix 'r' Command Block... (old syntax)");
                String radiusGroup = radiusMatcher.group();
                commandBlock.setCommand(commandBlock.getCommand().replace(radiusGroup, radiusGroup.replace("r=", "distance=..")));
                success = commandBlock.update();
            }
        }

        return success;
    }

    private static boolean isLConvertedToLevel(CommandBlock commandBlock, String exceptionMessage) {
        boolean success = false;
        if (exceptionMessage.contains("Unknown option 'l'")) {
            Matcher levelMatcher = level.matcher(commandBlock.getCommand());

            if (levelMatcher.find()) {
                logger.info("Attempting to fix 'l' Command Block... (old syntax)");
                String levelGroup = levelMatcher.group();
                commandBlock.setCommand(commandBlock.getCommand().replace(levelGroup, levelGroup.replace("l=", "level=..")));
                success = commandBlock.update();
            }
        }

        return success;
    }

    private static boolean isLmConvertedToLevel(CommandBlock commandBlock, String exceptionMessage) {
        boolean success = false;
        if (exceptionMessage.contains("Unknown option 'lm'")) {
            Matcher levelMoreMatcher = levelMore.matcher(commandBlock.getCommand());

            if (levelMoreMatcher.find()) {
                logger.info("Attempting to fix 'lm' Command Block... (old syntax)");
                String levelMoreGroup = levelMoreMatcher.group();
                commandBlock.setCommand(commandBlock.getCommand().replace(levelMoreGroup, levelMoreGroup.replace("lm=", "level=")+".."));
                success = commandBlock.update();
            }
        }

        return success;
    }
}
