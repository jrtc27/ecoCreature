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
package se.crafted.chrisb.ecoCreature.events;

import java.util.List;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import se.crafted.chrisb.ecoCreature.commons.EntityUtils;
import se.crafted.chrisb.ecoCreature.commons.EventUtils;
import se.crafted.chrisb.ecoCreature.settings.SpawnerMobTracking;

public final class EntityKilledEvent extends EntityDeathEvent
{
    private SpawnerMobTracking spawnerMobTracking;
    
    public static EntityKilledEvent createEvent(EntityDeathEvent event)
    {
        return new EntityKilledEvent(event.getEntity(), event.getDrops(), event.getDroppedExp());
    }

    private EntityKilledEvent(LivingEntity entity, List<ItemStack> drops, int droppedExp)
    {
        super(entity, drops, droppedExp);
    }

    public Player getKiller()
    {
        return EventUtils.getKillerFromDeathEvent(this);
    }

    public String getWeaponName()
    {
        return isTamedCreatureKill() ? getTamedCreature().getType().getName() : EntityUtils.getItemNameInHand(getKiller());
    }

    public boolean isTamedCreatureKill()
    {
        return getTamedCreature() != null;
    }

    private LivingEntity getTamedCreature()
    {
        return EventUtils.getTamedKillerFromDeathEvent(this);
    }

    public boolean isProjectileKill()
    {
        return EventUtils.isProjectileKill(this);
    }

    public SpawnerMobTracking getSpawnerMobTracking()
    {
        return spawnerMobTracking;
    }

    public void setSpawnerMobTracking(SpawnerMobTracking spawnerMobTracking)
    {
        this.spawnerMobTracking = spawnerMobTracking;
    }
}
