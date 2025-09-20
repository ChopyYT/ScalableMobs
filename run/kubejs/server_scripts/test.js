const ScalableMobsAPI = Java.loadClass("com.thomp.scalablemobs.api.ScalableMobsAPI");

EntityEvents.spawned(event => {
    let entity = event.entity;
    if (entity.type == "minecraft:wither") {
        let level = ScalableMobsAPI.getMobLevel(entity);
        let newlevel = 75
        ScalableMobsAPI.setMobLevel(entity, newlevel);
    }
});
