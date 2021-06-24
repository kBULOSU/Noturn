package com.noturn.stackdrops.misc.utils;

import com.google.common.collect.Maps;
import com.noturn.stackdrops.StackDropsPlugin;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TranslateItem {

    private static final Map<String, String> TRANSLATIONS = Maps.newHashMap();

    public TranslateItem() {
        Pattern pattern = Pattern.compile("^\\s*([\\w\\d\\.]+)\\s*=\\s*(.*)\\s*$");

        try {
            InputStream fis = StackDropsPlugin.INSTANCE.getResource("pt_BR.lang");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line;
            Matcher matcher;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.contains("=")) {
                    matcher = pattern.matcher(line);
                    if (matcher.matches()) {
                        TRANSLATIONS.put(matcher.group(1), matcher.group(2));
                    }
                }

            }
        } catch (IOException ignored) {
        }
    }

    public String get(ItemStack itemStack) {
        return get(itemStack, false, 1);
    }

    public String get(ItemStack itemStack, boolean isMoreThanOne, int amount) {
        net.minecraft.server.v1_8_R3.ItemStack nms = CraftItemStack.asNMSCopy(itemStack);
        String node = nms.a() + ".name";

        String val = TRANSLATIONS.get(node);

        if (val == null) {
            return node;
        }

        return (isMoreThanOne ? amount + "x " : "") + val;
    }

    private <T> T getFieldValue(Object obj, String fieldname) {
        Class<?> clazz = obj.getClass();
        do {
            try {
                Field field = clazz.getDeclaredField(fieldname);
                field.setAccessible(true);
                return (T) field.get(obj);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ignored) {
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        return null;
    }

}
