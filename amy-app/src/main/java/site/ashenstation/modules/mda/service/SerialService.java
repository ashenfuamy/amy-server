package site.ashenstation.modules.mda.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import site.ashenstation.entity.MdaSummary;
import site.ashenstation.modules.mda.dto.CreateSerialDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SerialService {


    public void createSerial(CreateSerialDto dto) {
        List<Integer> actors = dto.getActors();

        MdaSummary mdaSummary = new MdaSummary();
        BeanUtils.copyProperties(dto, mdaSummary);

        System.out.println(dto);

        System.out.println(mdaSummary);
    }
}
