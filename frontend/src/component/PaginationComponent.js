import * as React from "react";
import Pagination from "react-bootstrap/Pagination";


export default class PaginationComponent extends React.Component {

    handlePage = (value) => {
        this.props.handlePage(value);
    };

    handleSize = (value) => {
        this.props.handleSize(value);
    };


    render() {
        let items = [];
        let active = this.props.activePage;
        for(let number = 0; number < this.props.totalPages; number++) {
            let handleClick = () => { this.handlePage(number)};
            items.push(
                <Pagination.Item key={number} active={number === active} onClick={handleClick} >
                    {number+1}
                </Pagination.Item>,
            );
        }
        let sizes = [];
        let activeSize = this.props.size;
        for(let s = 1; s < 5; s++) {
            let handleClick = () => { this.handleSize(s*5)}  ;
            let number = s*5;
            sizes.push(
                <Pagination.Item key={s} active={number === activeSize} onClick={handleClick}>
                    {number}
                </Pagination.Item>
            )
        }

        return (
            <div className="d-flex justify-content-center">
                <aside className="mr-auto p-2">
                    <header>Page {this.props.activePage}</header>
                    <Pagination>{items}</Pagination>
                </aside>
                <aside className="p-2">
                    <header>Size</header>
                    <Pagination>{sizes}</Pagination>
                </aside>
            </div>
        )

    }
}