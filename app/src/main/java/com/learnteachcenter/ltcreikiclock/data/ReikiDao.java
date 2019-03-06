package com.learnteachcenter.ltcreikiclock.data;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;
import java.util.UUID;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * Created by aye2m on 10/12/17.
 */

@Dao
public interface ReikiDao {

    @Query("SELECT * FROM Reiki WHERE id = :id")
    LiveData<Reiki> getReikiById(String id);

    /**
     * Get all entities of type Reiki
     * @return
     */
    @Query("SELECT * FROM Reiki ORDER BY seqNo")
    LiveData<List<Reiki>> getReikis();


    /**
     * Insert a new Reiki
     * @param reiki
     */
    @Insert(onConflict = REPLACE)
    Long insertReiki(Reiki reiki);

    /**
     * Bulk update during rearrange operation
     * @param reikis
     */
    @Update(onConflict = REPLACE)
    void updateReiki(Reiki... reikis);

    /**
     * Delete a given Reiki
     * @param reiki
     */
    @Delete
    void deleteReiki(Reiki reiki);

    // Positions
    @Query("SELECT * FROM Position WHERE reikiId = :reikiId ORDER BY seqNo")
    LiveData<List<Position>> getPositionsByReikiId(String reikiId);

    @Query("SELECT * FROM Position WHERE reikiId = :reikiId AND seqNo = :seqNo")
    LiveData<Position> getPositionById(String reikiId, int seqNo);

    /**
     * Bulk update during rearrange operation
     * @param positions
     */
    @Update(onConflict = REPLACE)
    void updatePosition(Position... positions);

    /**
     * Insert a new Position
     * @param position
     */
    @Insert(onConflict = REPLACE)
    Long insertPosition(Position position);

    /**
     * Delete a given Position
     * @param position
     */
    @Delete
    void deletePosition(Position position);

    /*------ Following operations execute within the Repository which is running in background
    thread. In this case, it appears to be easier to return the actual values that LiveData version.
     */

    /**
     * Get all Reikis which have a seqNo value greater than argument. They'll be decremented in the
     * Repository afterwords.
     * @return
     */
    @Query("SELECT * FROM Reiki WHERE seqNo > :seqNo ORDER BY seqNo")
    List<Reiki> getReikisGreaterThanSeqNo(int seqNo);

    @Query("SELECT * FROM Position WHERE reikiId = :reikiId AND seqNo > :seqNo ORDER BY seqNo")
    List<Position> getPositionsGreaterThanSeqNo(String reikiId, int seqNo );

    /**
     * For use during rearrange operations. Avoid calling this method outside of Asynctask
     * background operation.
     * @param
     * @return
     */
    @Query("SELECT * FROM Position WHERE reikiId = :reikiId AND seqNo = :seqNo")
    Position getPositionByIdWithoutLiveData(String reikiId, int seqNo);

    /**
     * For use during rearrange operations. Avoid calling this method outside of Asynctask
     * background operation.
     * @param id
     * @return
     */
    @Query("SELECT * FROM Reiki WHERE id = :id")
    Reiki getReikiByIdWithoutLiveData(String id);
}
