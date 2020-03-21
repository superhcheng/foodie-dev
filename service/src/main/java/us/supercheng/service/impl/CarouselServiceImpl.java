package us.supercheng.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import us.supercheng.enums.YesOrNo;
import us.supercheng.mapper.CarouselMapper;
import us.supercheng.pojo.Carousel;
import us.supercheng.service.CarouselService;
import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> getActiveCarousel() {
        Example carouselExp = new Example(Carousel.class);
        Example.Criteria userCriteria = carouselExp.createCriteria();
        userCriteria.andEqualTo("isShow", YesOrNo.Yes.type);
        return this.carouselMapper.selectByExample(carouselExp);
    }
}
