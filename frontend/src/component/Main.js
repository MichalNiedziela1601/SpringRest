import * as React from "react";
import PollCard from "./PollCard";
import PaginationComponent from "./PaginationComponent";


export default class Main extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            polls: [],
            isLoaded: false,
            activePage: 0,
            size: 5
        }
    }

    componentDidMount() {
        // console.log("componentDidMount");
        this.getPolls(this.state.activePage, this.state.size);
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        console.log("update");
        if (prevState.activePage !== this.state.activePage) {
            this.getPolls(this.state.activePage, this.state.size);
        }
        if (prevState.size !== this.state.size) {
            this.getPolls(this.state.activePage, this.state.size);
        }
    }

    getPolls = (pageNum, size) => {
        fetch(`${process.env.REACT_APP_API_HOST}/polls?page=${pageNum}&size=${size}`)
            .then(res => res.json())
            .then(data => {
                // console.log(data);
                this.setState({
                    isLoaded: true,
                    polls: data.content,
                    totalPages: data.totalPages,
                    size: data.size,
                    totalElements: data.totalElements
                })
            })
            .catch(console.log);
    };

    handlePage = (value) => {
        this.setState({activePage: value});
    };

    handleSize = (value) => {
        this.setState({size: value, activePage: 0});
    };


    render() {
        if (!this.state.isLoaded || !this.state.polls) return null;
        const elements = this.state.polls.map((poll) =>
            <PollCard pollId={poll.id} key={poll.id} title={poll.question}/>
        );
        const size = this.state.size;
        const activePage = this.state.activePage;
        const totalPages = this.state.totalPages;
        return (
            <div>
                {elements}
                <PaginationComponent handlePage={this.handlePage} handleSize={this.handleSize} size={size}
                                     activePage={activePage} totalPages={totalPages}/>
            </div>

        )
    }
}