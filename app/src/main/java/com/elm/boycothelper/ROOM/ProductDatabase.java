package com.elm.boycothelper.ROOM;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {ProductEntity.class}, version = 1, exportSchema = false)
public abstract class ProductDatabase extends RoomDatabase {

    public abstract ProductDao productDao();


}
