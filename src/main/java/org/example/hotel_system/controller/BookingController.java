package org.example.hotel_system.controller;

import org.example.hotel_system.common.Result;
import org.example.hotel_system.entity.Booking;
import org.example.hotel_system.mapper.BookingMapper;
import org.example.hotel_system.service.BookingService;
import org.example.hotel_system.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@RequestMapping("/api/bookings")
public class BookingController {
    @Autowired
    BookingService bookingService;
//    @Autowired
//    RoomService roomService;
    /**
     * 保存新订单
     * <p>功能说明：接收前端传入的订单信息，调用服务层插入订单记录</p>
     * <p>业务逻辑：
     *   1. 验证房间是否存在
     *   2. 检查房间是否已被占用
     *   3. 插入订单记录并更新房间状态为"已占用"
     * </p>
     *
     * @param booking 订单实体对象，包含房间号、客人姓名、电话、入住日期等信息
     * @return Result 操作结果，成功返回订单数据，失败返回错误信息
     */
    @PostMapping("/save")
    public Result save(@RequestBody Booking booking) {
        int flag = bookingService.insert( booking);
        if(flag == 0) return Result.error("新增订单失败");
        return Result.success(booking);
    }
    /**
     * 多条件查询订单
     * <p>功能说明：根据传入的查询条件（客人姓名、房间号、订单状态）进行组合查询</p>
     * <p>查询规则：
     *   - 支持模糊查询客人姓名
     *   - 支持精确查询房间号
     *   - 支持按订单状态筛选
     *   - 所有条件均为可选，不传则查询全部
     * </p>
     *
     * @param booking 查询条件对象，可包含guestName、roomNumber、status等字段
     * @return Result 包含符合条件的订单列表
     */
    @GetMapping("/search")
    public Result<List<Booking>> findByCondition(@ModelAttribute Booking  booking) {
        // Spring 会自动将 URL 参数映射到对象的属性中
        return Result.success(bookingService.findByCondition(booking));
    }
    /**
     * 根据客人姓名查询订单
     * <p>功能说明：通过客人姓名进行模糊查询，返回所有匹配的订单记录</p>
     * <p>使用场景：快速查找某个客人的所有历史订单</p>
     *
     * @param name 客人姓名（支持模糊匹配）
     * @return Result 包含匹配到的订单列表
     */
    @GetMapping("/findByName")
    public Result<List<Booking>> findByName(@RequestParam String name) {
        return Result.success(bookingService.findByName(name));
    }
    /**
     * 办理退房结算
     * <p>功能说明：为指定订单办理退房手续，计算总费用并更新订单和房间状态</p>
     * <p>业务逻辑：
     *   1. 查询订单信息
     *   2. 计算入住天数（入住日期到当前时间）
     *   3. 根据房间单价计算总费用（不足一天按一天计算）
     *   4. 更新订单状态为"已退房"，记录退房时间和总金额
     *   5. 更新房间状态为"空闲"
     * </p>
     *
     * @param id 订单ID
     * @return Result 操作结果，成功返回提示信息
     */
    @PutMapping("/checkOut")
    public Result checkOut(@RequestParam Long id) {
        bookingService.checkOut(id);
        return Result.success("id为" + id + "的订单已退房");
    }
    /**
     * 下单预订房间
     * <p>功能说明：创建新的房间预订订单，将房间状态标记为"已预约"</p>
     * <p>与/save接口的区别：此接口专门用于预订场景，可能包含不同的业务逻辑</p>
     * <p>业务逻辑：
     *   1. 验证房间是否存在
     *   2. 检查房间是否已被占用或预约
     *   3. 创建订单记录
     *   4. 更新房间状态为"已预约"
     * </p>
     *
     * @param booking 订单实体对象，包含预订信息
     * @return Result 操作结果，成功返回订单数据
     */
    @PostMapping("/placeOrder")
    public Result placeOrder(@RequestBody Booking booking) {
        bookingService.placeOrder( booking);
        return Result.success(booking);
    }
    /**
     * 新增订单（通用接口）
     * <p>功能说明：与/save功能类似，提供另一种新增订单的接口方式</p>
     * <p>使用场景：可能用于不同的前端页面或调用场景</p>
     *
     * @param booking 订单实体对象
     * @return Result 操作结果，成功返回订单数据，失败返回错误信息
     */
    @PostMapping("/add")
    public Result insert(@RequestBody Booking booking) {
        int c = bookingService.insert(booking);
        if(c == 0) return Result.error("新增订单失败");
        else return Result.success(booking);
    }
    /**
     * 删除订单
     * <p>功能说明：根据订单ID删除订单记录</p>
     * <p>业务逻辑：
     *   1. 验证订单是否存在
     *   2. 删除订单记录
     *   3. 如果删除成功，将对应房间状态更新为"空闲"
     * </p>
     * <p>注意事项：删除操作不可逆，请谨慎使用</p>
     *
     * @param id 要删除的订单ID
     * @return Result 操作结果，成功返回提示信息，失败返回错误信息
     */
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long id) {
        int c = bookingService.delete(id);
        if(c == 0) return Result.error("删除订单失败: 该订单不存在或已删除");
        else return Result.success("删除id为" + id + "的订单成功");
    }
    /**
     * 更新订单信息
     * <p>功能说明：修改已存在订单的信息（如客人姓名、电话、入住日期等）</p>
     * <p>更新规则：
     *   - 只更新传入的非空字段
     *   - 订单ID必须存在
     *   - 不支持修改房间号（避免房间冲突）
     * </p>
     *
     * @param booking 订单实体对象，必须包含id字段，其他字段为要更新的内容
     * @return Result 操作结果，成功返回更新后的订单数据，失败返回错误信息
     */
    @PutMapping("/update")
    public Result update(@RequestBody Booking booking) {
        int c = bookingService.updateBooking(booking);
        if(c == 0) return Result.error("修改订单失败,id为" + booking.getId() + "的订单不存在或已删除");
        else return Result.success(booking);
    }
    /**
     * 查询所有订单
     * <p>功能说明：获取系统中所有订单的完整列表，不进行任何条件筛选</p>
     * <p>使用场景：数据导出、统计分析、全量数据展示等</p>
     * <p>注意事项：数据量大时可能影响性能，建议分页查询</p>
     *
     * @return Result 包含所有订单数据的列表
     */
    @GetMapping("/list")
    public Result<List<Booking>> list() {
        return Result.success(bookingService.findAll());
    }

    // 办理入住：将状态从 1(已预约) 改为 2(已入住)
    @PutMapping("/checkIn")
    public Result<String> checkIn(@RequestParam Long id) {
        bookingService.updateBookingStatus(2, id);
        return Result.success("办理入住成功");
    }

    // 取消预约：将状态从 1(已预约) 改为 0 或 4(已取消)，并释放房间
    @PutMapping("/cancel")
    public Result<String> cancel(@RequestParam Long id) {
        // 这里建议定义 4 为已取消，或者直接根据你的业务需求处理
        bookingService.updateBookingStatus(4, id);
        return Result.success("订单已取消");
    }

}
