(function ($) {
    "use strict";

    // Spinner
    var spinner = function () {
        setTimeout(function () {
            if ($('#spinner').length > 0) {
                $('#spinner').removeClass('show');
            }
        }, 1);
    };
    spinner();

    // Back to top button
    $(window).scroll(function () {
        if ($(this).scrollTop() > 300) {
            $('.back-to-top').fadeIn('slow');
        } else {
            $('.back-to-top').fadeOut('slow');
        }
    });
    $('.back-to-top').click(function () {
        $('html, body').animate({scrollTop: 0}, 1500, 'easeInOutExpo');
        return false;
    });


    // Sidebar Toggler
    $('.sidebar-toggler').click(function () {
        $('.sidebar, .content').toggleClass("open");
        return false;
    });


    // Progress Bar
    $('.pg-bar').waypoint(function () {
        $('.progress .progress-bar').each(function () {
            $(this).css("width", $(this).attr("aria-valuenow") + '%');
        });
    }, {offset: '80%'});


    // Calender
    $('#calender').datetimepicker({
        inline: true,
        format: 'L'
    });


    // Testimonials carousel
    $(".testimonial-carousel").owlCarousel({
        autoplay: true,
        smartSpeed: 1000,
        items: 1,
        dots: true,
        loop: true,
        nav: false
    });

    // Update registration stats chart
    function updateRegistrationStats() {
        // Check if chart element exists
        if ($('#registrationStatsChart').length === 0) {
            console.error('Chart element not found');
            return;
        }

        $.ajax({
            url: '/user/registration',
            method: 'GET',
            success: function (data) {
                // Check if chart instance exists
                if (!window.myChart2) {
                    console.error('Chart instance not initialized');
                    return;
                }

                // Check if required data properties exist
                if (data &&
                    typeof data.registeredStudents !== 'undefined' &&
                    typeof data.registeredTeachers !== 'undefined' &&
                    typeof data.totalStudents !== 'undefined' &&
                    typeof data.totalTeachers !== 'undefined') {

                    // Update chart data
                    window.myChart2.data.datasets[0].data = [data.registeredStudents, data.registeredTeachers];
                    window.myChart2.data.datasets[1].data = [data.totalStudents - data.registeredStudents, data.totalTeachers - data.registeredTeachers];
                    window.myChart2.data.datasets[2].data = [data.totalStudents, data.totalTeachers];
                    window.myChart2.update();
                } else {
                    console.error('Invalid data format from API');
                }
            },
            error: function (xhr, status, error) {
                console.error('Failed to fetch registration stats:', error);
            }
        });
    }

    // Initialize all charts
    function initCharts() {
        // Worldwide Sales Chart
        var ctx1 = $("#worldwide-sales").get(0).getContext("2d");
        window.myChart1 = new Chart(ctx1, {
            type: "bar",
            data: {
                labels: ["学生选题", "教师设题"],
                datasets: [{
                    label: "已提交",
                    data: [15, 95],
                    backgroundColor: "rgba(0, 156, 255, .7)"
                },
                    {
                        label: "未提交",
                        data: [8, 75],
                        backgroundColor: "rgba(0, 156, 255, .5)"
                    }
                ]
            },
            options: {
                responsive: true
            }
        });

        // Registration Stats Chart
        var ctx2 = $("#registrationStatsChart").get(0).getContext("2d");
        window.myChart2 = new Chart(ctx2, {
            type: "bar",
            data: {
                labels: ["学生", "教师"],
                datasets: [{
                    label: "已注册",
                    data: [0, 0],
                    backgroundColor: "rgba(0, 156, 255, .7)"
                },
                    {
                        label: "未注册",
                        data: [0, 0],
                        backgroundColor: "rgba(0, 156, 255, .5)"
                    },
                    {
                        label: "总数",
                        data: [0, 0],
                        backgroundColor: "rgba(0, 156, 255, .9)"
                    }],
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        max: 800
                    }
                }
            }
        });
    }

    // Initialize chart data
    $(document).ready(function () {
        // Initialize all charts
        initCharts();

        // Update registration stats
        updateRegistrationStats();
        setInterval(updateRegistrationStats, 300000); // Update every 5 minutes
    });

    // Automatically load topics after DOM is fully loaded
    $(window).on('load', function() {
        if (window.location.pathname === '/student/project-management') {
            searchTopics();
        }
    });

})(jQuery);

// Delete topic function
function deleteTopic(id) {
    if (confirm('确定要删除该课题吗？')) {
        $.ajax({
            url: '/teacher/delete-topic/' + id,
            type: 'DELETE',
            success: function (response) {
                alert(response);
                location.reload();
            },
            error: function (xhr) {
                alert('删除失败: ' + xhr.responseText);
            }
        });
    }
}

//Update topic function
function setEditForm(topicId) {
    fetch('/teacher/getTopic/' + topicId)
        .then(response => response.json())
        .then(data => {
            document.getElementById('editId').value = data.id;
            document.getElementById('editTitle').value = data.title;
            document.getElementById('editDescription').value = data.description;
            document.getElementById('editRequirement').value = data.requirements;
            document.getElementById('editMaxStudents').value = data.maxStudents;
        })
        .catch(error => console.error('Error:', error));
}

    // 学生页面搜索课题
    function searchTopics() {
        const status = document.querySelector('select').value;
        const keyword = document.querySelector('input[type="text"]').value.trim();
        
        let url = '/student/search-topics?';
        if (keyword) {
            url += `keyword=${encodeURIComponent(keyword)}&`;
        }
        
        // Add status filter
        if (status === 'available') {
            url += 'statuses=' + encodeURIComponent('2');
        } else if (status === 'selected') {
            // 已选状态需要查询student_topic表
            url = '/student/search-selected-topics?' + (keyword ? `keyword=${encodeURIComponent(keyword)}` : '');
        } else if (status === 'completed') {
            url += 'statuses=' + encodeURIComponent('3');
        } else {
            // 所有状态查询status为2和3的课题
            url += 'statuses=' + encodeURIComponent('2,3');
        }

    fetch(url)
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => {
                    throw new Error(err.message || 'Unknown error occurred');
                });
            }
            return response.json();
        })
        .then(data => {
            if (!data || !Array.isArray(data)) {
                throw new Error('Invalid data format received from server');
            }
            console.log('Received data:', data);
            updateTopicTable(data);
        })
        .catch(error => {
            console.error('Error:', error);
            alert('加载数据失败: ' + error.message);
        });
}

// Update topic table with search results
function updateTopicTable(topics) {
    const tbody = document.querySelector('#topicsTable tbody');
    tbody.innerHTML = '';

    if (!Array.isArray(topics)) {
        console.error('Expected array but got:', topics);
        return;
    }
    
    console.log('Raw topics data:', JSON.stringify(topics, null, 2));

    topics.forEach(topic => {
        try {
            const tr = document.createElement('tr');
            
            // Add ID
            const idTd = document.createElement('td');
            idTd.textContent = topic.id || 'N/A';
            tr.appendChild(idTd);

            // Add status
            const statusTd = document.createElement('td');
            const statusBadge = document.createElement('span');
            statusBadge.className = 'badge ' + (topic.status === 2 ? 'bg-success' :
                                               topic.status === 3 ? 'bg-warning' : 'bg-danger');
            statusBadge.textContent = topic.status === 2 ? '可选' : 
                                    topic.status === 3 ? '已满' : '已选';
            statusTd.appendChild(statusBadge);
            tr.appendChild(statusTd);

            // Add title
            const titleTd = document.createElement('td');
            titleTd.textContent = topic.title ? topic.title.substring(0, 5) + (topic.title.length > 5 ? '...' : '') : '无标题';
            titleTd.title = topic.title || '';
            tr.appendChild(titleTd);

            // Add description
            const descTd = document.createElement('td');
            descTd.textContent = topic.description ? topic.description.substring(0, 5) + (topic.description.length > 5 ? '...' : '') : '无描述';
            descTd.title = topic.description || '';
            tr.appendChild(descTd);

            // Add max students
            const maxStudentsTd = document.createElement('td');
            maxStudentsTd.textContent = topic.maxStudents || '0';
            tr.appendChild(maxStudentsTd);

            // Add requirements
            const reqTd = document.createElement('td');
            reqTd.textContent = topic.requirements ? topic.requirements.substring(0, 5) + (topic.requirements.length > 5 ? '...' : '') : '无特殊要求';
            reqTd.title = topic.requirements || '';
            tr.appendChild(reqTd);

            // Add teacher
            const teacherTd = document.createElement('td');
            teacherTd.textContent = topic.teacherName || '未知教师';
            tr.appendChild(teacherTd);

            // Add actions
            const actionTd = document.createElement('td');
            actionTd.className = 'd-flex gap-1';
            const detailBtn = document.createElement('button');
            detailBtn.className = 'btn btn-xs btn-primary';
            detailBtn.textContent = '查看详情';
            detailBtn.onclick = () => showTopicDetails(topic.id);
            actionTd.appendChild(detailBtn);
            
            if (topic.status === 2) {
                const selectBtn = document.createElement('button');
                selectBtn.className = 'btn btn-xs btn-success';
                selectBtn.textContent = '选择课题';
                selectBtn.onclick = () => selectTopic(topic.id);
                actionTd.appendChild(selectBtn);
            } else if (topic.status === 3) {
                const fullBtn = document.createElement('button');
                fullBtn.className = 'btn btn-xs btn-secondary';
                fullBtn.textContent = '课题已满';
                fullBtn.disabled = true;
                actionTd.appendChild(fullBtn);
            } else if (topic.status === 1) {
                const cancelBtn = document.createElement('button');
                cancelBtn.className = 'btn btn-sm btn-danger';
                cancelBtn.textContent = '取消选择';
                cancelBtn.onclick = () => cancelTopicSelection(topic.id);
                actionTd.appendChild(cancelBtn);
            }

            tr.appendChild(actionTd);
            tbody.appendChild(tr);
        } catch (error) {
            console.error('Error rendering topic row:', error);
        }
    });

    
    // Show message if no topics found
    if (topics.length === 0) {
        const tr = document.createElement('tr');
        const td = document.createElement('td');
        td.colSpan = 8;
        td.className = 'text-center';
        td.textContent = '未找到符合条件的课题';
        tr.appendChild(td);
        tbody.appendChild(tr);
    }
}

// Show topic details function
function showTopicDetails(topicId) {
    fetch('/student/get-topic-details/' + topicId)
        .then(response => {
            if (!response.ok) {
                throw new Error('获取详情失败: ' + response.statusText);
            }
            return response.json();
        })
        .then(data => {
            // 填充模态框内容
            document.getElementById('topicDetailsTitle').innerText = data.title;
            document.getElementById('topicDetailsDescription').innerText = data.description;
            document.getElementById('topicDetailsRequirements').innerText = data.requirements;
            document.getElementById('topicDetailsMaxStudents').innerText = data.maxStudents;
            document.getElementById('topicDetailsTeacher').innerText = data.teacherName;
            document.getElementById('topicDetailsStatus').innerText = 
                data.status === 2 ? '开放中' : data.status === 3 ? '已结束' : '未知状态';
            
            // 显示模态框
            $('#topicDetailsModal').modal('show');
        })
        .catch(error => {
            console.error('Error:', error);
            alert('获取课题详情失败: ' + error.message);
        });
}

// Select topic function
function selectTopic(topicId) {
    if (confirm('确定要选择该课题吗？')) {
        $.ajax({
            url: '/student/select-topic/' + topicId,
            type: 'POST',
            success: function(response) {
                alert(response);
                searchTopics();
            },
            error: function(xhr) {
                alert('选择课题失败: ' + xhr.responseText);
            }
        });
    }
}
