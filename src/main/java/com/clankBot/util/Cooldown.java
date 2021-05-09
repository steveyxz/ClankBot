package com.clankBot.util;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.util.Date;
import java.util.HashMap;

import static com.clankBot.Main.jda;
import static com.clankBot.Main.userDataManagerMongo;
import static com.clankBot.util.GlobalMethods.parseStringToMilli;

public class Cooldown {

    private String ID;

    /**
     * Creates a Cooldown instance USING THE FORMAT SHOWN BELOW.
     *
     * @param time The time specified in mute format, like 3m 4m 5s 3d,
     *             which would convert to 3 days, 7 minutes and 5 seconds..
     */
    public Cooldown(String time, JDA jda, String ID, boolean isNew) {
        this(parseStringToMilli(time), jda, ID, isNew);
    }

    public Cooldown(long milliseconds, JDA jda, String ID, boolean newCooldown) {
        this.ID = ID;
        if (newCooldown) {
            for (Guild g :
                    jda.getGuildCache()) {
                for (Member member : g.getMembers()) {
                    if (member.getUser().isBot()) {
                        continue;
                    }
                    String jsonText = (String) userDataManagerMongo.getValueOfKey("userData", "cooldowns", member.getId());
                    JsonParser parser = new JsonParser();

                    JsonObject object = parser.parse(jsonText).getAsJsonObject();
                    JsonObject valueObject = new JsonObject();
                    valueObject.add("time", new JsonPrimitive(milliseconds));
                    valueObject.add("timestamp", new JsonPrimitive(new Date().getTime()));
                    object.add(ID, valueObject);

                    jsonText = new Gson().toJson(object);
                    HashMap<String, Object> values = new HashMap<>();
                    values.put(member.getId(), jsonText);
                    userDataManagerMongo.insertToDocument("userData", "cooldowns", values);
                }
            }
        }
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public long getTimestamp(String userID, Guild e) {

        Member member = e.getMember(jda.retrieveUserById(userID).complete());

        assert member != null;
        if (member.getUser().isBot()) {
            return 0;
        }

        String jsonText = (String) userDataManagerMongo.getValueOfKey("userData", "cooldowns", member.getId());
        JsonParser parser = new JsonParser();

        JsonObject object = parser.parse(jsonText).getAsJsonObject();
        return object.get(ID).getAsJsonObject().get("timestamp").getAsLong();
    }

    public void setTimestamp(long timestamp, String userID, Guild e) {

        Member member = e.getMember(jda.retrieveUserById(userID).complete());

        assert member != null;
        if (member.getUser().isBot()) {
            return;
        }
        String jsonText = (String) userDataManagerMongo.getValueOfKey("userData", "cooldowns", member.getId());
        JsonParser parser = new JsonParser();

        JsonObject object = parser.parse(jsonText).getAsJsonObject();
        Long time = object.get(ID).getAsJsonObject().get("time").getAsLong();
        JsonObject valueObject = new JsonObject();
        valueObject.add("time", new JsonPrimitive(time));
        valueObject.add("timestamp", new JsonPrimitive(timestamp));
        object.remove(ID);
        object.add(ID, valueObject);

        jsonText = new Gson().toJson(object);
        HashMap<String, Object> values = new HashMap<>();
        values.put(member.getId(), jsonText);
        userDataManagerMongo.insertToDocument("userData", "cooldowns", values);
    }

    public long getMilliseconds(String userID, Guild e) {
        Member member = e.getMember(jda.retrieveUserById(userID).complete());

        assert member != null;
        if (member.getUser().isBot()) {
            return 0;
        }

        String jsonText = (String) userDataManagerMongo.getValueOfKey("userData", "cooldowns", member.getId());
        JsonParser parser = new JsonParser();

        JsonObject object = parser.parse(jsonText).getAsJsonObject();
        return object.get(ID).getAsJsonObject().get("time").getAsLong();
    }

    public void setMilliseconds(long milliseconds, String userID, Guild e) {
        Member member = e.getMember(jda.retrieveUserById(userID).complete());

        assert member != null;
        if (member.getUser().isBot()) {
            return;
        }
        String jsonText = (String) userDataManagerMongo.getValueOfKey("userData", "cooldowns", member.getId());
        JsonParser parser = new JsonParser();

        JsonObject object = parser.parse(jsonText).getAsJsonObject();
        object.remove(ID);
        long timestamp = object.get(ID).getAsJsonObject().get("timestamp").getAsLong();
        JsonObject valueObject = new JsonObject();
        valueObject.add("time", new JsonPrimitive(milliseconds));
        valueObject.add("time", new JsonPrimitive(timestamp));
        object.add(ID, valueObject);

        jsonText = new Gson().toJson(object);
        HashMap<String, Object> values = new HashMap<>();
        values.put(member.getId(), jsonText);
        userDataManagerMongo.insertToDocument("userData", "cooldowns", values);
    }

    public boolean checkCompletion(String userID, Guild e) {
        return new Date().getTime() >= getTimestamp(userID, e) + getMilliseconds(userID, e);
    }

    public long timeTillDone(String userID, Guild e) {
        return getMilliseconds(userID, e) - (new Date().getTime() - getTimestamp(userID, e));
    }
}
