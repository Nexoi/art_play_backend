var pdf_001 = {
    id: 001,
    info: {
        name: '',
        update_time: 'timestamp',
        //... pdf 信息
    },
    contents: [
        {
            // content one, 这些是段落信息
            text: '',
            page: {
                start_page_number: 7,
                end_page_number: 8,
            }
        },
    ],
    tables: [
        {
            // table one, 这里是表格信息
            title: '',
            table: {
                keys: [],
                headers: [],
                values: {}
            },
            page: {
                start_page_number: 7,
                end_page_number: 8,
            }
        }
    ]
}
var company = {
    name: '',
    code: '',
    update_time: 'timestamp',
    pdfs: [
        pdf_001, pdf_001, pdf_001, pdf_001,
    ]
}
