/*
 * This file is part of ecoCreature.
 *
 * Copyright (c) 2011-2012, R. Ramos <http://github.com/mung3r/>
 * ecoCreature is licensed under the GNU Lesser General Public License.
 *
 * ecoCreature is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * ecoCreature is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package se.crafted.chrisb.ecoCreature.settings;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import se.crafted.chrisb.ecoCreature.rewards.Reward;
import se.crafted.chrisb.ecoCreature.rewards.gain.PlayerGain;
import se.crafted.chrisb.ecoCreature.rewards.parties.Party;

public class WorldSettings implements SpawnerMobTracking
{
    private boolean clearOnNoDrops;
    private boolean overrideDrops;
    private boolean noFarm;
    private boolean noFarmFire;

    private List<AbstractRewardSettings> rewardSettings;
    private Set<PlayerGain> gainMultipliers;
    private Set<Party> parties;

    private Set<Integer> spawnerMobs;
    private boolean canCampSpawner;
    private boolean campByEntity;

    public WorldSettings()
    {
        rewardSettings = new ArrayList<AbstractRewardSettings>();
        gainMultipliers = Collections.emptySet();
        parties = Collections.emptySet();

        spawnerMobs = new HashSet<Integer>();
    }

    public boolean isClearOnNoDrops()
    {
        return clearOnNoDrops;
    }

    public void setClearOnNoDrops(boolean clearOnNoDrops)
    {
        this.clearOnNoDrops = clearOnNoDrops;
    }

    public boolean isOverrideDrops()
    {
        return overrideDrops;
    }

    public void setOverrideDrops(boolean overrideDrops)
    {
        this.overrideDrops = overrideDrops;
    }

    public boolean isNoFarm()
    {
        return noFarm;
    }

    public void setNoFarm(boolean noFarm)
    {
        this.noFarm = noFarm;
    }

    public boolean isNoFarmFire()
    {
        return noFarmFire;
    }

    public void setNoFarmFire(boolean noFarmFire)
    {
        this.noFarmFire = noFarmFire;
    }

    public void setGainMultipliers(Set<PlayerGain> gainMultipliers)
    {
        this.gainMultipliers = gainMultipliers;
    }

    public void setParties(Set<Party> parties)
    {
        this.parties = parties;
    }

    public void setRewardSettings(List<AbstractRewardSettings> rewardSettings)
    {
        this.rewardSettings = rewardSettings;
    }

    public boolean hasReward(Event event)
    {
        boolean hasReward = false;

        for (AbstractRewardSettings settings : this.rewardSettings) {
            if (settings.hasRewardSource(event)) {
                hasReward = true;
                break;
            }
        }

        return hasReward;
    }

    public Reward createReward(Event event)
    {
        for (AbstractRewardSettings settings : this.rewardSettings) {
            if (settings.hasRewardSource(event)) {
                return settings.getRewardSource(event).createReward(event);
            }
        }

        return null;
    }

    public double getGainMultiplier(Player player)
    {
        double multiplier = 1.0;

        for (PlayerGain gain : gainMultipliers) {
            multiplier *= gain.getMultiplier(player);
        }

        return multiplier;
    }

    public Set<String> getParty(Player player)
    {
        Set<String> players = new HashSet<String>();

        for (Party party : parties) {
            if (party.isShared()) {
                players.addAll(party.getPlayers(player));
            }
        }

        return players;
    }

    @Override
    public void addSpawnerMob(LivingEntity entity)
    {
        // Only add to the array if we're tracking by entity. Avoids a memory leak.
        if (!canCampSpawner && campByEntity) {
            spawnerMobs.add(Integer.valueOf(entity.getEntityId()));
        }
    }

    @Override
    public boolean isSpawnerMob(LivingEntity entity)
    {
        return spawnerMobs.remove(Integer.valueOf(entity.getEntityId()));
    }

    public void setCanCampSpawner(boolean canCampSpawner)
    {
        this.canCampSpawner = canCampSpawner;
    }

    public void setCampByEntity(boolean campByEntity)
    {
        this.campByEntity = campByEntity;
    }
}
