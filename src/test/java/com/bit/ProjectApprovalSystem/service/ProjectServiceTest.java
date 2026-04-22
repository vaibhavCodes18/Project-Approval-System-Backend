package com.bit.ProjectApprovalSystem.service;

import com.bit.ProjectApprovalSystem.repository.ProjectRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    private ProjectRepository projectRepository;
    @Disabled
    @Test
    void testProject(){
        assertEquals(19, 9+10);
        assertNotNull(projectRepository.findById(new ObjectId("69dcaeaa69ebc60560958ebf")));
    }

}
