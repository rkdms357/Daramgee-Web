package asset.dao;

import asset.dto.AssetDTO;
import util.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AssetDAO {
    // 1. 전체 목록 조회
    public List<AssetDTO> selectAll() {
        List<AssetDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        String sql = "select asset_id, name, current_price, change_rate from asset order by name";
        try {
            conn = DBUtil.dbconnect();
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            while (rs.next()) {
                AssetDTO asset = new AssetDTO();
                asset.setAssetId(rs.getString("asset_id"));
                asset.setName(rs.getString("name"));
                asset.setCurrentPrice(rs.getBigDecimal("current_price"));
                asset.setChangeRate(rs.getDouble("change_rate"));
                list.add(asset);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.dbDisconnect(conn, st, rs);
        }
        return list;
    }

    // 2. 가격, 변화율 업데이트 (Batch Update)
    public void batchUpdatePriceAndRate(Connection conn, List<AssetDTO> assets) {
        if (assets == null || assets.isEmpty()) return;
        String sql = "update asset set current_price = ?, change_rate = ? where asset_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            for (AssetDTO asset : assets) {
                ps.setBigDecimal(1, asset.getCurrentPrice());
                ps.setDouble(2, asset.getChangeRate());
                ps.setString(3, asset.getAssetId());
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}