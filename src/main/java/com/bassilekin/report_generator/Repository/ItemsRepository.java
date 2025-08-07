package com.bassilekin.report_generator.Repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bassilekin.report_generator.Model.Item;
import java.util.List;
import com.bassilekin.report_generator.Model.User;


public interface ItemsRepository extends JpaRepository <Item, Long>{
    
    List<Item> findByUser(User user);

}
